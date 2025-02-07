package com.xxl.cache.core.factory;

import com.xxl.cache.core.broadcast.CacheBroadcastMessage;
import com.xxl.cache.core.cache.CacheManager;
import com.xxl.cache.core.cache.CacheTypeEnum;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.redis.RedisCache;
import com.xxl.cache.core.redis.RedisManager;
import com.xxl.cache.core.serialize.SerializerTypeEnum;
import com.xxl.cache.core.util.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryJedisPubSub;

import java.nio.charset.StandardCharsets;

/**
 * xxl-cache factory
 *
 * @author xuxueli 2025-02-03
 */
public class XxlCacheFactory {
    private static Logger logger = LoggerFactory.getLogger(XxlCacheFactory.class);

    // ---------------------- instance ----------------------
    private static XxlCacheFactory xxlConfFactory;
    public static XxlCacheFactory getInstance() {
        if (xxlConfFactory == null) {
            throw new RuntimeException("xxl-cache factory not init.");
        }
        return xxlConfFactory;
    }

    // -------------------- base --------------------

    private String l1Provider = CacheTypeEnum.CAFFEINE.getType();
    private int maxSize;
    private long expireAfterWrite;

    private String l2Provider = CacheTypeEnum.REDIS.getType();
    private String serializer = SerializerTypeEnum.JAVA.getType();
    private String nodes;
    private String user;
    private String password;

    public XxlCacheFactory() {
        // instance
        xxlConfFactory = this;
    }

    public void setL1Provider(String l1Provider) {
        this.l1Provider = l1Provider;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setExpireAfterWrite(long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public void setL2Provider(String l2Provider) {
        this.l2Provider = l2Provider;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // -------------------- start --------------------

    private CacheManager l1CacheManager;
    private RedisManager l2CacheManager;

    public CacheManager getL1CacheManager() {
        return l1CacheManager;
    }

    public RedisManager getL2CacheManager() {
        return l2CacheManager;
    }

    /**
     * start
     */
    public void start() {

        // l1 cache
        if (!CacheTypeEnum.CAFFEINE.getType().equals(l1Provider)) {
            throw new RuntimeException("xxl-cache l1 cache provider invalid, l1Provider="+l1Provider);
        }
        l1CacheManager = new CacheManager(CacheTypeEnum.CAFFEINE, maxSize, expireAfterWrite);
        l1CacheManager.start();

        // l2 cache
        if (!CacheTypeEnum.REDIS.getType().equals(l2Provider)) {
            throw new RuntimeException("xxl-cache l2 cache provider invalid, l2Provider="+l2Provider);
        }
        l2CacheManager = new RedisManager(serializer, nodes, user, password);
        l2CacheManager.start();

        // broadcast, l1 > l2 > l1
        subscribe();
        logger.info(">>>>>>>>>>> xxl-cache, XxlCacheFactory start finish.");
    }

    public void stop() {
        // l1 cache
        if (l1CacheManager != null) {
            l1CacheManager.stop();
        }

        // l2 cache
        if (l2CacheManager != null) {
            l2CacheManager.stop();
        }

        // broadcast
        if (jedisPubSub != null) {
            jedisPubSub.unsubscribe();
        }
        if (broadcastListenerThread != null) {
            try {
                if (broadcastListenerThread.getState() != Thread.State.TERMINATED){
                    // interrupt and wait
                    broadcastListenerThread.interrupt();
                    try {
                        broadcastListenerThread.join();
                    } catch (Throwable e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.info(">>>>>>>>>>> xxl-cache, XxlCacheFactory stop finish.");
    }

    // -------------------- broadcast --------------------

    private final String channel = "xxl-cache-channel";
    private Thread broadcastListenerThread;
    private BinaryJedisPubSub jedisPubSub;

    /**
     * subscribe
     */
    public void subscribe() {

        // jedisPubSub
        jedisPubSub = new BinaryJedisPubSub() {
            @Override
            public void onMessage(byte[] channel, byte[] message) {

                // deserialize message
                CacheBroadcastMessage broadcastMessage = SerializerTypeEnum.JAVA.getSerializer().deserialize(message);

                // refresh by key
                String finalKey = CacheUtil.generateKey(broadcastMessage.getCategory(), broadcastMessage.getKey());

                // load l2
                CacheValue l2CacheValue = XxlCacheFactory.getInstance().getL2CacheManager().getCache().get(finalKey);
                l2CacheValue = l2CacheValue!=null?l2CacheValue:new CacheValue(null);

                // fill l1
                XxlCacheFactory.getInstance().getL1CacheManager().getCache(broadcastMessage.getCategory()).set(finalKey, l2CacheValue);
                logger.debug(">>>>>>>>>>> xxl-cache, broadcast set l1-cache, key: {}, value: {}", finalKey, l2CacheValue);
            }

            @Override
            public void onSubscribe(byte[] channel, int subscribedChannels) {
                logger.debug(">>>>>>>>>>> xxl-cache, Subscribed to channel " + new String(channel, StandardCharsets.UTF_8) + ", total channels: " + subscribedChannels);
            }

            @Override
            public void onUnsubscribe(byte[] channel, int subscribedChannels) {
                logger.debug(">>>>>>>>>>> xxl-cache, Unsubscribed to channel " + new String(channel, StandardCharsets.UTF_8) + ", total channels: " + subscribedChannels);
            }
        };

        // thread
        broadcastListenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RedisCache redisCache = (RedisCache) l2CacheManager.getCache();
                redisCache.subscribe(channel, jedisPubSub);
            }
        });
        broadcastListenerThread.setDaemon(true);
        broadcastListenerThread.setName("broadcastListenerThread");
        broadcastListenerThread.start();
    }

    /**
     * broadcast
     *
     * @param message
     */
    public void broadcast(CacheBroadcastMessage message) {
        RedisCache redisCache = (RedisCache) l2CacheManager.getCache();
        redisCache.publish(channel, message);
    }

}
