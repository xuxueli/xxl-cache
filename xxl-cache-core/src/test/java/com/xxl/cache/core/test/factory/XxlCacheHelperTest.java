package com.xxl.cache.core.test.factory;

import com.xxl.cache.core.XxlCacheHelper;
import com.xxl.cache.core.cache.CacheTypeEnum;
import com.xxl.cache.core.factory.XxlCacheFactory;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class XxlCacheHelperTest {

    @Test
    public void testXxlCacheHelperCRUD() throws InterruptedException {
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

        // 先清除，避免 Redis 有缓存，影响测试
        xxlCache.del(key);
        // 写操作对应：本地 L1 写 --> L2 写 --> 广播消息 --> 本地节点收到消息，再写一次 L1
        TimeUnit.SECONDS.sleep(3);

        String val = xxlCache.get(key);
        assertNull(val);

        xxlCache.set(key, "jack333");
        TimeUnit.SECONDS.sleep(3);

        // 存换操作后，比对
        val = xxlCache.get(key);
        assertEquals("jack333", val);

        // 清理
        xxlCache.del(key);
        TimeUnit.SECONDS.sleep(3);

        val = xxlCache.get(key);
        assertNull(val);

        // stop
        xxlCacheFactory.stop();
    }

}
