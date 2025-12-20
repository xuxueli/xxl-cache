package com.xxl.cache.core.cache;

import com.xxl.cache.core.caffeine.CaffeineCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * cache manager
 *
 * @author xuxueli 2025-02-04
 */
public class CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
    private final CacheTypeEnum cacheType;
    private final int maxSize;
    private final long expireAfterWrite;

    public CacheManager() {
        this(CacheTypeEnum.CAFFEINE, 10000, 600);
    }

    public CacheManager(CacheTypeEnum cacheType, int maxSize, long expireAfterWrite) {
        this.cacheType = cacheType != null ? cacheType : CacheTypeEnum.CAFFEINE;
        this.maxSize = maxSize > 0 ? maxSize : 10000;
        this.expireAfterWrite = expireAfterWrite > 0 ? expireAfterWrite : 600;
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
     * @param category
     * @return
     */
    public Cache getCache(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        return cacheMap.computeIfAbsent(category, key -> createCache(cacheType));
    }


    /**
     * create cache
     *
     * @return
     */
    private Cache createCache(CacheTypeEnum cacheEnum) {
        if (CacheTypeEnum.CAFFEINE == cacheEnum) {
            return new CaffeineCache(maxSize, expireAfterWrite, TimeUnit.SECONDS);
        }
        throw new RuntimeException("Cache type not supported");
    }

}
