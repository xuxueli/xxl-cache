//package com.xxl.cache.core.util.tmp;
//
//import com.xxl.cache.core.broadcast.CacheBroadcastListener;
//import com.xxl.cache.core.l1.caffeine.CaffeineCache;
//import com.xxl.cache.core.l2.redis.RedisCache;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * xxl-cache factory
// *
// * @author xuxueli 2025-02-03
// */
//public class XxlCacheFactory {
//
//    // -------------------- base --------------------
//
//    private String l1Provider;
//    private String l2Provider;
//    private String namespace;
//    private String broadcastTopic;
//
//    private RedisTemplate<String, Object> redisTemplate;
//    private RedisMessageListenerContainer redisMessageListenerContainer;
//
//    public XxlCacheFactory(String l1Provider,
//                           String l2Provider,
//                           String namespace,
//                           String broadcastTopic,
//                           RedisTemplate<String, Object> redisTemplate,
//                           RedisMessageListenerContainer redisMessageListenerContainer) {
//
//        this.l1Provider = l1Provider;
//        this.l2Provider = l2Provider;
//        this.namespace = namespace;
//        this.broadcastTopic = broadcastTopic;
//        this.redisTemplate = redisTemplate;
//        this.redisMessageListenerContainer = redisMessageListenerContainer;
//
//        // start
//        start();
//    }
//
//    public String getNamespace() {
//        return namespace;
//    }
//
//    // -------------------- init --------------------
//
//    private RedisCache redisCache;
//    private CaffeineCache caffeineCache;
//
//    public CaffeineCache getCaffeineCache() {
//        return caffeineCache;
//    }
//
//    public RedisCache getRedisCache() {
//        return redisCache;
//    }
//
//    /**
//     * init
//     */
//    public void start() {
//        // l1: caffeineCache
//        this.caffeineCache = new CaffeineCache(10*10000, 3*60, TimeUnit.SECONDS);
//
//        // l2: redisCache
//        this.redisCache = new RedisCache(redisTemplate, redisMessageListenerContainer);
//
//        // broadcast listener
//        redisCache.subscribe(broadcastTopic, new CacheBroadcastListener(this));
//    }
//
//}
