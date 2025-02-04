package com.xxl.cache.core.test.redis;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.redis.RedisManager;

public class RedisManagerTest {

    public static void main(String[] args) {

        String nodes = "127.0.0.1:6379";
        String user = null;
        String password = null;

        RedisManager l2CacheManager = new RedisManager(nodes, user, password);
        l2CacheManager.start();

        Cache cache = l2CacheManager.getCache();

        String key = "user03";

        cache.del(key);
        System.out.println(cache.get(key));;

        cache.set(key, new CacheValue("jack03"));
        System.out.println(cache.get(key));;

    }

}
