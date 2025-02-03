package com.xxl.cache.core.util;

/**
 * cache util
 *
 * @author xuxueli 2025-02-03
 */
public class CacheUtil {


    /**
     * generate cache key
     *
     * @param   category
     * @param   key
     * @return  "category:key"
     */
    public static String generateKey(String category, String key) {
        if (category==null || category.trim().isEmpty()) {
            throw new RuntimeException("category is null");
        }
        if (key==null || key.trim().isEmpty()) {
            throw new RuntimeException("key is null");
        }

        return category + ":" + key;
    }

}
