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
        // 所以每次写操作后，需要等待完成全流程的写操作，否则会影响测试结果
        TimeUnit.SECONDS.sleep(3);

        String val = xxlCache.get(key);
        assertNull(val);

        xxlCache.set(key, "jack333");
        TimeUnit.SECONDS.sleep(3);
        // 比如上述 del、set 都不等待的话，这里读到可能是 null
        // 因为实际的触发流程是： del -->  set -->  执行收到的 del  --> get。数据不一致了！
        // todo 可能是个 bug:？ 即如果连续操作缓存，可能因为广播的延迟，导致读到脏数据！
        val = xxlCache.get(key);
        assertEquals("jack333", val);


        xxlCache.del(key);
        TimeUnit.SECONDS.sleep(3);

        val = xxlCache.get(key);
        assertNull(val);

        // stop
        xxlCacheFactory.stop();
    }

}
