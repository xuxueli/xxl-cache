package com.xxl.cache.core.redis;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.serialize.Serializer;
import com.xxl.cache.core.serialize.SerializerTypeEnum;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

/**
 * redis cache
 *
 * @author xuxueli 2025-02-03
 */
public class RedisManager {
    private static Logger logger = LoggerFactory.getLogger(RedisManager.class);

    private String serializerType;
    private String nodes;
    private String user;
    private String password;

    private Serializer serializer = SerializerTypeEnum.JAVA.getSerializer();
    private Set<HostAndPort> clusterNodes = new HashSet<>();
    private int connectionTimeout = 2000;
    private int soTimeout = 2000;
    private int maxAttempts = 3;


    public RedisManager(String serializerType, String nodes, String user, String password) {
        this.serializerType = serializerType;
        this.nodes = nodes;
        if (user!=null && !user.trim().isEmpty()) {
            this.user = user;
        }
        if (password!=null && !password.trim().isEmpty()) {
            this.password = password;
        }

        // parse
        SerializerTypeEnum serializerTypeEnum = SerializerTypeEnum.match(this.serializerType);
        if (serializerTypeEnum != null) {
            serializer = serializerTypeEnum.getSerializer();
        }
        if (nodes!=null && !nodes.trim().isEmpty()) {
            for (String node : nodes.split(",")) {
                String[] parts = node.split(":");
                if (parts.length == 2) {
                    String host = parts[0];
                    int port = Integer.parseInt(parts[1]);
                    clusterNodes.add(new HostAndPort(host, port));
                }
            }
        }
    }


    // ---------------------- start / stop ----------------------

    private JedisPool jedisPool;
    private JedisCluster jedisCluster;
    private RedisCache defaultRedisCache;

    /**
     * start
     */
    public void start() {
        if (clusterNodes==null || clusterNodes.isEmpty()){
            throw new IllegalArgumentException("clusterNodes can not be null or empty.");
        }

        // pooled client
        if (clusterNodes.size() > 1) {
            try {
                GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig();
                JedisClientConfig clientConfig = DefaultJedisClientConfig
                        .builder()
                        .timeoutMillis(soTimeout)
                        .connectionTimeoutMillis(connectionTimeout)
                        .user(user)
                        .password(password)
                        .build();
                jedisCluster = new JedisCluster(clusterNodes, clientConfig, maxAttempts, poolConfig);
                logger.info(">>>>>>>>>>> xxl-cache, RedisManager (JedisCluster) initialized successfully.");
            } catch (Exception e) {
                logger.error(">>>>>>>>>>> RedisManager (JedisCluster) initialized error.", e);
                throw new RuntimeException("RedisManager (JedisCluster) initialized error.", e);
            }
        } else {
            try {
                JedisPoolConfig poolConfig = new JedisPoolConfig();
                JedisClientConfig clientConfig = DefaultJedisClientConfig
                        .builder()
                        .timeoutMillis(soTimeout)
                        .connectionTimeoutMillis(connectionTimeout)
                        .user(user)
                        .password(password)
                        .build();

                jedisPool = new JedisPool(poolConfig, clusterNodes.stream().findFirst().get(), clientConfig);
                logger.info(">>>>>>>>>>> xxl-cache, RedisManager (JedisPool) initialized successfully.");
            } catch (Exception e) {
                logger.error(">>>>>>>>>>> RedisManager (JedisPool) initialized error.", e);
                throw new RuntimeException("RedisManager (JedisPool) initialized error.", e);
            }
        }

        // cache
        defaultRedisCache = new RedisCache(jedisPool, jedisCluster, serializer);
    }

    /**
     * stop
     */
    public void stop() {
        try {
            if (jedisCluster!=null) {
                jedisCluster.close();
            }
            if (jedisPool!=null) {
                jedisPool.close();
            }
            logger.info(">>>>>>>>>>> xxl-cache, RedisManager stop finish.");
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> RedisManager stop error.", e);
        }
    }


    // ---------------------- tool ----------------------

    /**
     * get client
     * @return
     */
    public Cache getCache() {
        return defaultRedisCache;
    }

}
