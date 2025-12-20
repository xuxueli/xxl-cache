package com.xxl.cache.core.cache;

import com.xxl.cache.core.caffeine.CaffeineCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * cache manager, for l1 cache
 *
 * @author xuxueli 2025-02-04
 */
public class CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private volatile CacheTypeEnum cacheType = CacheTypeEnum.CAFFEINE;      // cache type
    private int maxSize = 10000;                                            // max size of cache
    private long expireAfterWrite = 60 * 10;                                // expire time, by second

    public CacheManager() {
    }
    public CacheManager(CacheTypeEnum cacheType, int maxSize, long expireAfterWrite) {
        if (cacheType != null) {
            this.cacheType = cacheType;
        }
        if (maxSize > 0) {
            this.maxSize = maxSize;
        }
        if (expireAfterWrite > 0) {
            this.expireAfterWrite = expireAfterWrite;
        }
    }

    /**
     * start
     */
    public void start() {
        // do nothing
    }

    /**
     * stop
     */
    public void stop() {
        // do nothing
    }

    /**
     * get cache, create if cache not exist
     *
     * @param category cache category
     * @return  cache
     */
    public Cache getCache(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        return cacheMap.computeIfAbsent(category, key -> createCache(cacheType));

        /**
         * Cache cache = cacheMap.get(category);
         * if (cache == null) {
         *     synchronized (this) {
         *         cache = cacheMap.get(category);
         *         if (cache == null) {
         *             try {
         *                 cache = createCache(cacheType);
         *                 cacheMap.put(category, cache);
         *             } catch (Exception e) {
         *                 throw new RuntimeException("Failed to create cache for category: " + category, e);
         *             }
         *         }
         *     }
         * }
         * return cache;
         */
    }


    /**
     * create cache
     *
     * @return  cache
     */
    private Cache createCache(CacheTypeEnum cacheEnum) {
        if (CacheTypeEnum.CAFFEINE == cacheEnum) {
            return new CaffeineCache(maxSize, expireAfterWrite, TimeUnit.SECONDS);
        }
        throw new RuntimeException("Cache type not supported");
    }

}
