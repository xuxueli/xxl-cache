package com.xxl.cache.core.test.cache;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.cache.CacheManager;
import com.xxl.cache.core.cache.CacheTypeEnum;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.util.CacheUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CacheManagerTest {

    @Test
    public void testCacheManagerCRUD() {
        String category = "user";
        String useId = "id001";

        // 创建缓存管理器
        CacheManager cacheManager = new CacheManager(CacheTypeEnum.CAFFEINE, -1, -1);
        Cache cache = cacheManager.getCache(category);
        String key = CacheUtil.generateKey(category, useId);
        CacheValue cacheValue = new CacheValue("Alice");

        // 设置值
        cache.set(key, cacheValue);

        // 获取值并验证
        CacheValue retrievedValue = cache.get(key);
        assertNotNull(retrievedValue);
        assertEquals("Alice", retrievedValue.getValue());

        // 检查键是否存在并验证
        assertTrue(cache.exists(key));

        // 删除键并验证
        cache.del(key);
        // 缓存不存在，返回的是 null
        assertNull(cache.exists(key));

        // 清空缓存并验证
        //cache.clear();
        //assertEquals(0, cache.size());
    }

}
