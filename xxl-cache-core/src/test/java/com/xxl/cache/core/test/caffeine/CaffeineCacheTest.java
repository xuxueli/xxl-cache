package com.xxl.cache.core.test.caffeine;

import com.xxl.cache.core.cache.CacheValue;
import com.xxl.cache.core.caffeine.CaffeineCache;
import com.xxl.cache.core.util.CacheUtil;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheTest {

    public static void main(String[] args) {
        String category = "user";
        String useId = "id001";

        // 创建缓存实例
        CaffeineCache caffeineCache = new CaffeineCache(100, 10, TimeUnit.MINUTES);

        String key = CacheUtil.generateKey(category, useId);

        // 设置值
        caffeineCache.set(key, new CacheValue("Alice"));

        // 获取值
        System.out.println(caffeineCache.get(key)); // 输出: Alice

        // 检查键是否存在
        System.out.println(caffeineCache.exists(key)); // 输出: true

        // 删除键
        caffeineCache.del(key);
        System.out.println(caffeineCache.exists(key)); // 输出: false

        // 清空缓存
        //caffeineCache.clear();
        //System.out.println(caffeineCache.size()); // 输出: 0
    }

}
