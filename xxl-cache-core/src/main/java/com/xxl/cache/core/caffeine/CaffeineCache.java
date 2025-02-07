package com.xxl.cache.core.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.cache.CacheValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * caffeine cache
 *
 * @author xuxueli 2025-02-03
 */
public class CaffeineCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(CaffeineCache.class);

    private final com.github.benmanes.caffeine.cache.Cache<String, CacheValue> cache;

    /**
     * caffeine cache
     *
     * @param maxSize
     * @param expireAfterWrite  Only to control the local cache data synchronization, does not affect the data expiration
     * @param timeUnit
     */
    public CaffeineCache(int maxSize, long expireAfterWrite, TimeUnit timeUnit) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireAfterWrite, timeUnit)   // all same expire, only for sync-all
                .build();
    }

    @Override
    public void set(String key, CacheValue cacheValue) {
        cache.put(key, cacheValue);
    }

    @Override
    public CacheValue get(String key) {
        CacheValue cacheValue = cache.getIfPresent(key);
        return cacheValue;
    }

    @Override
    public void del(String key) {
        cache.invalidate(key);
    }

    @Override
    public Boolean exists(String key) {
        //boolean exists = cache.getIfPresent(key) != null;
        CacheValue cacheValue = get(key);
        Boolean exists = cacheValue==null ?null: (cacheValue.getValue()!=null && cacheValue.isValid());

        return exists;
    }

    /*@Override
    public void clear() {
        cache.invalidateAll();
        logger.debug("Cache cleared");
    }

    @Override
    public long size() {
        long size = cache.estimatedSize();
        logger.debug("Cache size: {}", size);
        return size;
    }*/

}
