package com.xxl.cache.core.cache;

import com.xxl.cache.core.caffeine.CaffeineCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class CacheManager {

    private volatile ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
    private volatile CacheTypeEnum cacheType = CacheTypeEnum.CAFFEINE;

    public CacheManager() {
    }
    public CacheManager(CacheTypeEnum cacheType) {
        if (cacheType != null) {
            this.cacheType = cacheType;
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
     * @param category
     * @return
     */
    public Cache getCache(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        Cache cache = cacheMap.get(category);
        if (cache == null) {
            synchronized (this) {
                cache = cacheMap.get(category);
                if (cache == null) {
                    try {
                        cache = createCache(cacheType);
                        cacheMap.put(category, cache);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create cache for category: " + category, e);
                    }
                }
            }
        }
        return cache;
    }


    /**
     * create cache
     *
     * @return
     */
    private Cache createCache(CacheTypeEnum cacheEnum) {
        if (CacheTypeEnum.CAFFEINE == cacheEnum) {
            return new CaffeineCache(100, 10, TimeUnit.MINUTES);
        } else {
            throw new RuntimeException("Cache type not supported");
        }
    }

}
