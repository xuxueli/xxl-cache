package com.xxl.cache.core.test.caffeine;

import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.caffeine.CaffeineCache;
import com.xxl.cache.core.util.CacheUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class CaffeineCacheTest {

    @Test
    public void testCaffeineCacheCRUD() {
        String category = "user";
        String useId = "id001";

        CaffeineCache caffeineCache = new CaffeineCache(100, 10, TimeUnit.MINUTES);
        String key = CacheUtil.generateKey(category, useId);
        CacheValue cacheValue = new CacheValue("Alice");

        // 设置值
        caffeineCache.set(key, cacheValue);

        // 验证取值
        CacheValue retrievedValue = caffeineCache.get(key);
        assertNotNull(retrievedValue);
        assertEquals("Alice", retrievedValue.getValue());

        // 验证存在状态
        assertTrue(caffeineCache.exists(key));

        // 删除验证
        caffeineCache.del(key);
        assertNull(caffeineCache.exists(key));

        // 清空缓存验证
        //caffeineCache.clear();
        //assertEquals(0, caffeineCache.size());
    }
}
