package com.xxl.cache.core.redis;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.serialize.Serializer;
import com.xxl.cache.core.serialize.SerializerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;

/**
 * redis cache
 *
 * @author xuxueli 2025-02-03
 */
public class RedisCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final UnifiedJedis jedisClient;
    private final Serializer serializer;
    public RedisCache(UnifiedJedis jedisClient, Serializer serializer) {
        this.jedisClient = jedisClient;
        this.serializer = serializer;
    }

    @Override
    public void set(String key, CacheValue cacheValue) {
        try {
            // serialize value
            byte[] valueBytes = serializer.serialize(cacheValue);

            // set value
            if (cacheValue.getSurvivalTime() < 0) {
                // set value, and never expire
                jedisClient.set(key.getBytes(StandardCharsets.UTF_8), valueBytes);
            } else {
                // set value, with survivalTime
                jedisClient.psetex(key.getBytes(StandardCharsets.UTF_8), cacheValue.getSurvivalTime(), valueBytes);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public CacheValue get(String key) {
        try {
            // get
            byte[] valueBytes = jedisClient.get(key.getBytes(StandardCharsets.UTF_8));
            if (valueBytes == null) {
                return null;
            }

            // deserialize value
            return serializer.deserialize(valueBytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void del(String key) {
        try {
            jedisClient.del(key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Boolean exists(String key) {
        try {
            return jedisClient.exists(key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /*@Override
    public void clear() {
        throw new RuntimeException("not support");
    }

    @Override
    public long size() {
        throw new RuntimeException("not support");
    }*/

    // plugin

    /**
     * publish
     *
     * @param channel channel
     * @param message message
     */
    public void publish(String channel, Object message) {
        // serialize message
        byte[] messageBytes = SerializerTypeEnum.JAVA.getSerializer().serialize(message);

        // broadcast message to channel
        try {
            jedisClient.publish(channel.getBytes(StandardCharsets.UTF_8), messageBytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * subscribe
     *
     * @param channel channel
     * @param jedisPubSub jedisPubSub
     */
    public void subscribe(String channel, BinaryJedisPubSub jedisPubSub) {
        try {
            // subscribe channel
            jedisClient.subscribe(jedisPubSub, channel.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
