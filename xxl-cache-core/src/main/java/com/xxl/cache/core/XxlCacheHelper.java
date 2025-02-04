package com.xxl.cache.core;

import com.xxl.cache.core.broadcast.CacheBroadcastMessage;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.factory.XxlCacheFactory;
import com.xxl.cache.core.util.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class XxlCacheHelper {
    private static final Logger logger = LoggerFactory.getLogger(XxlCacheHelper.class);

    private static final ConcurrentMap<String, XxlCache> cacheMap = new ConcurrentHashMap<>();

    /**
     * get cache by category
     *
     * @param category
     * @return
     */
    public static XxlCache getCache(String category, long survivalTime) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        XxlCache cache = cacheMap.get(category);
        if (cache == null) {
            synchronized (cacheMap) {
                cache = cacheMap.get(category);
                if (cache == null) {
                    try {
                        cache = new XxlCache(category, survivalTime);
                        cacheMap.put(category, cache);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create cache for category: " + category, e);
                    }
                }
            }
        }
        return cache;
    }

    public static XxlCache getCache(String category) {
        return getCache(category, -1);
    }

    /**
     * xxl cache
     */
    public static class XxlCache {

        private String category;
        private long survivalTime;      // -1 never expire;
        public XxlCache(String category, long survivalTime) {
            this.category = category;
            this.survivalTime = survivalTime;
        }
        public XxlCache(String category) {
            this.category = category;
            this.survivalTime = -1;
        }

        /**
         * set cache
         *
         * @param key
         * @param value
         */
        public void set(String key, Object value) {
            String finalKey = CacheUtil.generateKey(category, key);
            CacheValue cacheValue = new CacheValue(value, survivalTime);

            // l1 cache
            XxlCacheFactory.getInstance().getL1CacheManager().getCache(category).set(finalKey, cacheValue);
            logger.debug(">>>>>>>>>>> xxl-cache, set l1-cache, key: {}, value: {}", finalKey, cacheValue);

            // l2 cache
            XxlCacheFactory.getInstance().getL2CacheManager().getCache().set(finalKey, cacheValue);
            logger.debug(">>>>>>>>>>> xxl-cache, set l2-cache, key: {}, value: {}", finalKey, cacheValue);

            // broadcast, l1 > l2 > l1
            XxlCacheFactory.getInstance().broadcast(new CacheBroadcastMessage(category, key));
        }

        /**
         * get cache
         *
         * @param key
         * @return
         */
        public Object get(String key) {
            String finalKey = CacheUtil.generateKey(category, key);

            // l1 cache
            CacheValue l1CacheValue = XxlCacheFactory.getInstance().getL1CacheManager().getCache(category).get(finalKey);
            if (l1CacheValue != null) {
                if (l1CacheValue.isValid()) {       // l1-cache, not support key-expore, need valid time
                    logger.debug(">>>>>>>>>>> xxl-cache, get from l1-cache, key: {}, value: {}", finalKey, l1CacheValue);
                    return l1CacheValue.getValue();
                }
                return null;
            }

            // l2 cache
            CacheValue l2CacheValue = XxlCacheFactory.getInstance().getL2CacheManager().getCache().get(finalKey);
            logger.debug(">>>>>>>>>>> xxl-cache, get from l2-cache, key: {}, value: {}", finalKey, l1CacheValue);
            if (l2CacheValue != null) {

                // fill l1
                XxlCacheFactory.getInstance().getL1CacheManager().getCache(category).set(finalKey, l2CacheValue);
                logger.debug(">>>>>>>>>>> xxl-cache, lazy set l2-cache, key: {}, value: {}", finalKey, l2CacheValue);
                return l2CacheValue.getValue();
            } else {

                // fill l1
                XxlCacheFactory.getInstance().getL1CacheManager().getCache(category).set(finalKey, new CacheValue(null));   // l1-cache, none-value
                logger.debug(">>>>>>>>>>> xxl-cache, lazy set l2-cache, key: {}, value: {}", finalKey, null);
                return null;
            }
        }

        /**
         * del
         *
         * @param key
         */
        public void del(String key) {
            String finalKey = CacheUtil.generateKey(category, key);

            // l1 cache
            XxlCacheFactory.getInstance().getL1CacheManager().getCache(category).del(finalKey);
            logger.debug(">>>>>>>>>>> xxl-cache, del l1-cache, key: {}", finalKey);

            // l2 cache
            XxlCacheFactory.getInstance().getL2CacheManager().getCache().del(finalKey);
            logger.debug(">>>>>>>>>>> xxl-cache, del l2-cache, key: {}", finalKey);

            // broadcast, l1 > l2 > l1
            XxlCacheFactory.getInstance().broadcast(new CacheBroadcastMessage(category, key));
        }

        /**
         * exists
         *
         * @param key
         * @return
         */
        public boolean exists(String key) {
            String finalKey = CacheUtil.generateKey(category, key);

            // l1 cache
            Boolean l1value = XxlCacheFactory.getInstance().getL1CacheManager().getCache(category).exists(finalKey);
            if (l1value == null) {

                // fill l1 from l2
                get(key);
            }

            // l1 cache
            l1value = XxlCacheFactory.getInstance().getL1CacheManager().getCache(category).exists(finalKey);

            return l1value;
        }

    }

}