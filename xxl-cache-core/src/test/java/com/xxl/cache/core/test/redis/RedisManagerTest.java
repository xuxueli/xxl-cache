package com.xxl.cache.core.test.redis;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.redis.RedisManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class RedisManagerTest {
    private static final Logger logger = LoggerFactory.getLogger(RedisManagerTest.class);

    @Test
    public void testRedisCRUD() {
        String nodes = "127.0.0.1:6379";
        RedisManager l2CacheManager = new RedisManager(null, nodes, null, null, null);
        l2CacheManager.start();

        Cache cache = l2CacheManager.getCache();
        String key = "user03";

        // 清理
        cache.del(key);

        // 测试写入
        cache.set(key, new CacheValue("jack03"));
        logger.info("set key:{}, value:{}", key, cache.get(key).getValue());

        // 测试读取
        String cacheValue = cache.get(key).getValue().toString();
        logger.info("get key:{}, value:{}", key, cacheValue);
        assertEquals("jack03", cacheValue);

        // 测试重复删除
        cache.del(key);
        logger.info("delete key:{}", key);
        assertNull(cache.get(key));
    }

}
