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
     * @param key
     * @param cacheValue
     */
    void set(String key, CacheValue cacheValue);

    /**
     * get cache value
     *
     * @param key
     * @return
     */
    CacheValue get(String key);

    /**
     * delete value
     *
     * @param
     * @return
     */
    void del(String key);

    /**
     * exists key
     *
     * @param
     * @return
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