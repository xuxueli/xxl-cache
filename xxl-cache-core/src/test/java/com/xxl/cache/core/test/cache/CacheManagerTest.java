package com.xxl.cache.core.test.cache;

import com.xxl.cache.core.cache.Cache;
import com.xxl.cache.core.cache.CacheManager;
import com.xxl.cache.core.cache.CacheTypeEnum;
import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.util.CacheUtil;

public class CacheManagerTest {

    public static void main(String[] args) {
        String category = "user";
        String useId = "id001";

        // 创建缓存管理器
        CacheManager cacheManager = new CacheManager(CacheTypeEnum.CAFFEINE);
        Cache cache = cacheManager.getCache(category);

        String key = CacheUtil.generateKey(category, useId);

        // 设置值
        cache.set(key, new CacheValue("Alice"));

        // 获取值
        System.out.println(cache.get(key)); // 输出: Alice

        // 检查键是否存在
        System.out.println(cache.exists(key)); // 输出: true

        // 删除键
        cache.del(key);
        System.out.println(cache.exists(key)); // 输出: false

        // 清空缓存
        /*cache.clear();
        System.out.println(cache.size()); // 输出: 0*/
    }

}
