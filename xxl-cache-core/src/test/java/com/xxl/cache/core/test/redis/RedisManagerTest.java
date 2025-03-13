package com.xxl.cache.core.test.redis;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.redis.RedisManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RedisManagerTest {

    @Test
    public void testRedisCRUD() {
        String nodes = "127.0.0.1:6379";
        RedisManager l2CacheManager = new RedisManager(null, nodes, null, null);
        l2CacheManager.start();

        Cache cache = l2CacheManager.getCache();
        String key = "user03";

        // 测试删除
        cache.del(key);
        assertNull(cache.get(key));

        // 测试写入和读取
        cache.set(key, new CacheValue("jack03"));
        assertEquals("jack03", cache.get(key).getValue());

        // 测试重复删除
        cache.del(key);
        assertNull(cache.get(key));
    }

}
