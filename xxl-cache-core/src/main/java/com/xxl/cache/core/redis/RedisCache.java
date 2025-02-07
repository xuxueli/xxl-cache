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
    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final JedisPool jedisPool;
    private final JedisCluster jedisCluster;
    private final Serializer serializer;
    public RedisCache(JedisPool jedisPool, JedisCluster jedisCluster, Serializer serializer) {
        this.jedisPool = jedisPool;
        this.jedisCluster = jedisCluster;
        this.serializer = serializer;
    }

    @Override
    public void set(String key, CacheValue cacheValue) {
        if (jedisCluster!=null) {
            try {
                byte[] valueBytes = serializer.serialize(cacheValue);
                if (cacheValue.getSurvivalTime() < 0) {
                    jedisCluster.set(key.getBytes(StandardCharsets.UTF_8), valueBytes);
                } else {
                    jedisCluster.psetex(key.getBytes(StandardCharsets.UTF_8), cacheValue.getSurvivalTime(), valueBytes);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                byte[] valueBytes = serializer.serialize(cacheValue);
                if (cacheValue.getSurvivalTime() < 0) {
                    jedis.set(key.getBytes(StandardCharsets.UTF_8), valueBytes);
                } else {
                    jedis.psetex(key.getBytes(StandardCharsets.UTF_8), cacheValue.getSurvivalTime(), valueBytes);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public CacheValue get(String key) {
        if (jedisCluster!=null) {
            try {
                byte[] valueBytes = jedisCluster.get(key.getBytes(StandardCharsets.UTF_8));
                if (valueBytes == null) {
                    return null;
                }

                return serializer.deserialize(valueBytes);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                byte[] valueBytes = jedis.get(key.getBytes(StandardCharsets.UTF_8));
                if (valueBytes == null) {
                    return null;
                }

                return serializer.deserialize(valueBytes);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public void del(String key) {
        if (jedisCluster!=null) {
            try {
                jedisCluster.del(key.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.del(key.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Boolean exists(String key) {
        if (jedisCluster!=null) {
            try {
                return jedisCluster.exists(key.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.exists(key.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return false;
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
     * @param channel
     * @param message
     */
    public void publish(String channel, Object message) {
        // serialize message
        byte[] messageBytes = SerializerTypeEnum.JAVA.getSerializer().serialize(message);

        // invoke
        if (jedisCluster!=null) {
            try {
                jedisCluster.publish(channel.getBytes(StandardCharsets.UTF_8), messageBytes);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel.getBytes(StandardCharsets.UTF_8), messageBytes);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * subscribe
     *
     * @param channel
     * @param jedisPubSub
     */
    public void subscribe(String channel, BinaryJedisPubSub jedisPubSub) {
        if (jedisCluster!=null) {
            try {
                jedisCluster.subscribe(jedisPubSub, channel.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(jedisPubSub, channel.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


}
