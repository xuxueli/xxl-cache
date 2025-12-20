package com.xxl.cache.core.cache;

/**
 * cache interface
 *
 * @author xuxueli 2025-02-03
 */
public interface Cache {

    /**
     * set value
     *
     * @param key key
     * @param cacheValue cache value
     */
    void set(String key, CacheValue cacheValue);

    /**
     * get cache value
     *
     * @param key key
     * @return cache value
     */
    CacheValue get(String key);

    /**
     * delete value
     *
     * @param key
     */
    void del(String key);

    /**
     * exists key
     *
     * @param key key
     * @return true if exists, false if not exists; null unknown
     */
    Boolean exists(String key);

   /* *//**
     *
     *//*
    void clear();

    *//**
     * 获取缓存中的键值对数量。
     *
     * @return
     *//*
    long size();*/

}