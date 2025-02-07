package com.xxl.cache.core.test.factory;

import com.xxl.cache.core.XxlCacheHelper;
import com.xxl.cache.core.cache.CacheTypeEnum;
import com.xxl.cache.core.factory.XxlCacheFactory;

import java.util.concurrent.TimeUnit;

public class XxlCacheHelperTest {

    public static void main(String[] args) throws InterruptedException {
        // start
        XxlCacheFactory xxlCacheFactory = new XxlCacheFactory();
        xxlCacheFactory.setL1Provider(CacheTypeEnum.CAFFEINE.getType());
        xxlCacheFactory.setL2Provider(CacheTypeEnum.REDIS.getType());
        xxlCacheFactory.setNodes("127.0.0.1:6379");
        xxlCacheFactory.setPassword(null);

        xxlCacheFactory.start();

        // test
        String category = "user";
        String key = "user03";

        XxlCacheHelper.XxlCache xxlCache = XxlCacheHelper.getCache(category);

        xxlCache.del(key);
        String val = xxlCache.get(key);
        System.out.println(val);

        xxlCache.set(key, "jack333");
        val = xxlCache.get(key);
        System.out.println(val);

        TimeUnit.SECONDS.sleep(3);

        // stop
        xxlCacheFactory.stop();
    }

}
