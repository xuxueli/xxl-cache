package com.xxl.cache.core.redis;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.serialize.Serializer;
import com.xxl.cache.core.serialize.SerializerTypeEnum;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * redis cache
 *
 * @author xuxueli 2025-02-03
 */
public class RedisManager {
    private static Logger logger = LoggerFactory.getLogger(RedisManager.class);

    private Serializer serializer = SerializerTypeEnum.JAVA.getSerializer();
    private Set<HostAndPort> clusterNodes = new HashSet<>();
    private String user;
    private String password;
    private int database = 0;
    private int clientTimeout = 2000;
    private int poolTimeout = 2000;
    private int maxAttempts = 3;

    public RedisManager(String serializerType, String nodes, String user, String password, Integer database) {
        // parse serializer
        SerializerTypeEnum serializerTypeEnum = SerializerTypeEnum.match(serializerType);
        if (serializerTypeEnum != null) {
            serializer = serializerTypeEnum.getSerializer();
        }
        // parse clusterNodes
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
        // parse user
        if (user!=null && !user.trim().isEmpty()) {
            this.user = user.trim();
        }
        // parse password
        if (password!=null && !password.trim().isEmpty()) {
            this.password = password.trim();
        }
        // parse database
        if (database!=null && database>0) {
            this.database = database;
        }
    }


    // ---------------------- start / stop ----------------------

    // jedis client
    private UnifiedJedis jedisClient;
    // redis cache
    private RedisCache defaultRedisCache;

    /**
     * start
     */
    public void start() {
        // valid
        if (clusterNodes==null || clusterNodes.isEmpty()){
            throw new IllegalArgumentException("clusterNodes can not be null or empty.");
        }

        // build config
        JedisClientConfig clientConfig = DefaultJedisClientConfig
                .builder()
                .timeoutMillis(clientTimeout)
                .user(user)
                .password(password)
                .database(database)
                .build();

        // build poolConfig
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxWait(Duration.ofMillis(poolTimeout));        // get pool timeout

        // build jedisClient
        if (clusterNodes.size() > 1) {
            try {
                jedisClient = RedisClusterClient
                        .builder()
                        .nodes(clusterNodes)                            // cluster node
                        .maxAttempts(maxAttempts)                       // maxAttempts
                        .poolConfig(poolConfig)         // pool config
                        .clientConfig(clientConfig)
                        .build();

                logger.info(">>>>>>>>>>> xxl-cache, RedisManager (RedisClusterClient) initialized successfully.");
            } catch (Exception e) {
                logger.error(">>>>>>>>>>> RedisManager (RedisClusterClient) initialized error.", e);
                throw new RuntimeException("RedisManager (RedisClusterClient) initialized error.", e);
            }
        } else {
            try {
                jedisClient = RedisClient
                        .builder()
                        .hostAndPort(clusterNodes.stream().findFirst().get())   // host and port
                        .poolConfig(poolConfig)                 // pool config
                        .clientConfig(clientConfig)                             // client config, such as: timeout、password、database
                        .build();
                logger.info(">>>>>>>>>>> xxl-cache, RedisManager (RedisClient) initialized successfully.");
            } catch (Exception e) {
                logger.error(">>>>>>>>>>> RedisManager (RedisClient) initialized error.", e);
                throw new RuntimeException("RedisManager (RedisClient) initialized error.", e);
            }
        }

        // build redisClient
        defaultRedisCache = new RedisCache(jedisClient, serializer);
    }

    /**
     * stop
     */
    public void stop() {
        try {
            if (jedisClient!=null) {
                jedisClient.close();
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
