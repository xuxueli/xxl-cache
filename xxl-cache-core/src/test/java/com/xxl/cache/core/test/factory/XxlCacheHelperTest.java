package com.xxl.cache.core.test.factory;

import com.xxl.cache.core.XxlCacheHelper;
import com.xxl.cache.core.cache.CacheTypeEnum;
import com.xxl.cache.core.factory.XxlCacheFactory;

import java.util.concurrent.TimeUnit;

public class XxlCacheHelperTest {

    public static void main(String[] args) throws InterruptedException {
        // start
        XxlCacheFactory xxlCacheFactory = new XxlCacheFactory();
        xxlCacheFactory.setL1Provider(CacheTypeEnum.CAFFEINE.getName());
        xxlCacheFactory.setL2Provider(CacheTypeEnum.REDIS.getName());
        xxlCacheFactory.setNodes("127.0.0.1:6379");
        xxlCacheFactory.setPassword(null);

        xxlCacheFactory.start();

        // test
        String category = "user";
        String key = "user03";

        XxlCacheHelper.getCache(category).del(key);
        System.out.println(XxlCacheHelper.getCache(category).get(key));

        XxlCacheHelper.getCache(category).set(key, "jack333");
        System.out.println(XxlCacheHelper.getCache(category).get(key));

        TimeUnit.SECONDS.sleep(3);

        // stop
        xxlCacheFactory.stop();
    }

}
