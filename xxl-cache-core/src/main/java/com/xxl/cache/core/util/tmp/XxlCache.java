//package com.xxl.cache.core.tmp;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.xxl.cache.core.cache.ICache;
//import com.xxl.cache.core.util.GsonTool;
//import io.lettuce.core.RedisClient;
//import io.lettuce.core.api.StatefulRedisConnection;
//import io.lettuce.core.api.sync.RedisCommands;
//
//import java.util.concurrent.TimeUnit;
//
//public class XxlCache<K, V> implements ICache<K, V> {
//
//    private final Cache<K, V> localCache;
//    private final RedisCommands<String, String> redisCommands;
//    private final String cacheName;
//
//    public XxlCache(String redisUri, String cacheName) {
//        this.localCache = Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .build();
//        this.cacheName = cacheName;
//
//        RedisClient redisClient = RedisClient.create(redisUri);
//        StatefulRedisConnection<String, String> connection = redisClient.connect();
//        this.redisCommands = connection.sync();
//
//    }
//
//    @Override
//    public V get(K key) {
//        V value = localCache.getIfPresent(key);
//        if (value == null) {
//            String redisValue = redisCommands.hget(cacheName, key.toString());
//            if (redisValue != null) {
//                value = GsonTool.fromJson(redisValue, V);
//                localCache.put(key, value);
//            }
//        }
//        return value;
//    }
//
//    @Override
//    public void put(K key, V value) {
//        localCache.put(key, value);
//
//        String serializeData = GsonTool.toJson(value);
//        redisCommands.hset(cacheName, key.toString(), serializeData);
//    }
//
//    @Override
//    public void evict(K key) {
//        localCache.invalidate(key);
//        redisCommands.hdel(cacheName, key.toString());
//    }
//
//    @Override
//    public void clear() {
//        localCache.invalidateAll();
//        redisCommands.del(cacheName);
//    }
//
//
//
//    private static class CacheUpdateEvent<K, V> {
//        private final K key;
//        private final V value;
//
//        public CacheUpdateEvent(K key, V value) {
//            this.key = key;
//            this.value = value;
//        }
//
//        public K getKey() {
//            return key;
//        }
//
//        public V getValue() {
//            return value;
//        }
//    }
//
//    private static class CacheEvictEvent<K> {
//        private final K key;
//
//        public CacheEvictEvent(K key) {
//            this.key = key;
//        }
//
//        public K getKey() {
//            return key;
//        }
//    }
//
//}