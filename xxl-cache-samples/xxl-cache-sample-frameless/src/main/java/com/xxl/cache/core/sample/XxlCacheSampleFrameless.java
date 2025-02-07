package com.xxl.cache.core.sample;

import com.xxl.cache.core.XxlCacheHelper;
import com.xxl.cache.core.factory.XxlCacheFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class XxlCacheSampleFrameless {

    public static void main(String[] args) throws InterruptedException {
        // init and start
        XxlCacheFactory xxlCacheFactory = initAndStart();

        // test
        String category = "user";
        String key = "user03";

        /**
         * 1、定义缓存对象，并指定 “缓存category + 过期时间”
         */
        XxlCacheHelper.XxlCache xxlCache = XxlCacheHelper.getCache(category);

        /**
         * 2、缓存删：按照 L1 -> L2 顺序依次删缓存，同时借助内部广播机制更新全局L1节点缓存；
         */
        xxlCache.del(key);
        System.out.println((String) xxlCache.get(key));

        /**
         * 3、缓存写：按照 L1 -> L2 顺序依次写缓存，同时借助内部广播机制更新全局L1节点缓存；
         */
        xxlCache.set(key, "jack333");

        /**
         * 4、缓存读：按照 L1 -> L2 顺序依次读取缓存，如果L1存在缓存则返回，否则读取L2缓存并同步L1；
         */
        System.out.println((String) xxlCache.get(key));

        TimeUnit.SECONDS.sleep(30);

        // stop
        xxlCacheFactory.stop();
    }

    private static XxlCacheFactory initAndStart(){
        // load config
        Properties prop = loadClassPathProp("xxl-cache.properties");

        // start
        XxlCacheFactory xxlCacheFactory = new XxlCacheFactory();
        xxlCacheFactory.setL1Provider(prop.getProperty("xxl.cache.l1.provider"));
        xxlCacheFactory.setMaxSize(Integer.valueOf(prop.getProperty("xxl.cache.l1.maxSize")));
        xxlCacheFactory.setExpireAfterWrite(Long.valueOf(prop.getProperty("xxl.cache.l1.expireAfterWrite")));
        xxlCacheFactory.setL2Provider(prop.getProperty("xxl.cache.l2.provider"));
        xxlCacheFactory.setSerializer(prop.getProperty("xxl.cache.l2.serializer"));
        xxlCacheFactory.setNodes(prop.getProperty("xxl.cache.l2.nodes"));
        xxlCacheFactory.setUser(prop.getProperty("xxl.cache.l2.user"));
        xxlCacheFactory.setPassword(prop.getProperty("xxl.cache.l2.password"));

        xxlCacheFactory.start();

        return xxlCacheFactory;
    }

    public static Properties loadClassPathProp(String propertyFileName) {
        try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName)){
            if (in == null) {
                return null;
            }
            Properties prop = new Properties();
            prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            return prop;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
