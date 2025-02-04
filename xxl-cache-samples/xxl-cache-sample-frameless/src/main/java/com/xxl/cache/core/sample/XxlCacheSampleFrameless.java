package com.xxl.cache.core.sample;

import com.xxl.cache.core.XxlCacheHelper;
import com.xxl.cache.core.cache.CacheTypeEnum;
import com.xxl.cache.core.factory.XxlCacheFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class XxlCacheSampleFrameless {

    public static void main(String[] args) throws InterruptedException {
        // load config
        Properties prop = loadClassPathProp("xxl-cache.properties");

        // start
        XxlCacheFactory xxlCacheFactory = new XxlCacheFactory();
        xxlCacheFactory.setL1Provider(prop.getProperty("xxl.cache.l1.provider"));
        xxlCacheFactory.setL2Provider(prop.getProperty("xxl.cache.l2.provider"));
        xxlCacheFactory.setNodes(prop.getProperty("xxl.cache.l2.nodes"));
        xxlCacheFactory.setPassword(prop.getProperty("xxl.cache.l2.password"));

        xxlCacheFactory.start();

        // test
        String category = "user";
        String key = "user03";

        XxlCacheHelper.getCache(category).del(key);
        System.out.println(XxlCacheHelper.getCache(category).get(key));

        XxlCacheHelper.getCache(category).set(key, "jack333");
        System.out.println(XxlCacheHelper.getCache(category).get(key));

        TimeUnit.SECONDS.sleep(300);

        // stop
        xxlCacheFactory.stop();
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
