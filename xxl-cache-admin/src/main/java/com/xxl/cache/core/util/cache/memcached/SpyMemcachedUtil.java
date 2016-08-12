package com.xxl.cache.core.util.cache.memcached;

import com.xxl.cache.core.util.PropertiesUtil;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.xxl.cache.core.util.PropertiesUtil.DEFAULT_CONFIG;

/**
 * Memcached客户端工具类(Base on spymemcached)
 * Created by xuxueli on 16/8/11.
 *
 *  <!-- spymemcached -->
 *  <dependency>
 *      <groupId>net.spy</groupId>
 *      <artifactId>spymemcached</artifactId>
 *      <version>2.12.1</version>
 *  </dependency>
 *
 */
public class SpyMemcachedUtil {
    private static Logger logger = LogManager.getLogger();

    private static long SHUTDOWN_TIMEOUT_MS = 2500;
    private static long UPDATE_TIMEOUT_MS = 2500;

    private static MemcachedClient memcachedClient;
    private static ReentrantLock INSTANCE_INIT_LOCL = new ReentrantLock(false);
    private static MemcachedClient getInstance(){
        if (memcachedClient == null){
            try {
                if (INSTANCE_INIT_LOCL.tryLock(2, TimeUnit.SECONDS)){
                    try {
                        Properties prop = PropertiesUtil.loadProperties(DEFAULT_CONFIG);
                        String servers = PropertiesUtil.getString(prop, "spymemcached.address");

                        memcachedClient = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(servers));
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                }
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        return memcachedClient;
    }
    public static void destroy() throws Exception {
        if (memcachedClient != null) {
            memcachedClient.shutdown(SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        }
    }


    /**
     * Get方法, 转换结果类型并屏蔽异常, 仅返回Null.
     */
    public static <T> T  get(String key) {
        try {
            return (T) getInstance().get(key);
        } catch (RuntimeException e) {
            logger.error("", e);
            return null;
        }
    }

    /**
     * GetBulk方法, 转换结果类型并屏蔽异常.
     */
    public static <T> Map<String, T> getBulk(Collection<String> keys) {
        try {
            return (Map<String, T>) getInstance().getBulk(keys);
        } catch (RuntimeException e) {
            logger.error("", e);
            return null;
        }
    }

    /**
     * 异步Set方法, 不考虑执行结果.
     */
    public static void set(String key, Object value, int expiredTime) {
        getInstance().set(key, expiredTime, value);
    }

    /**
     * 安全的Set方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
     */
    public static boolean safeSet(String key, int expiration, Object value) {
        Future<Boolean> future = getInstance().set(key, expiration, value);
        try {
            return future.get(UPDATE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("", e);
            future.cancel(false);
            return false;
        }
    }

    /**
     * 异步 Delete方法, 不考虑执行结果.
     */
    public static void delete(String key) {
        getInstance().delete(key);
    }

    /**
     * 安全的Delete方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
     */
    public static boolean safeDelete(String key) {
        Future<Boolean> future = getInstance().delete(key);
        try {
            return future.get(UPDATE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return false;
    }

    /**
     * Incr方法.
     */
    public static long incr(String key, int by, long defaultValue) {
        return getInstance().incr(key, by, defaultValue);
    }

    /**
     * Decr方法.
     */
    public static long decr(String key, int by, long defaultValue) {
        return getInstance().decr(key, by, defaultValue);
    }

    /**
     * 异步Incr方法, 不支持默认值, 若key不存在返回-1.
     */
    public static Future<Long> asyncIncr(String key, int by) {
        return getInstance().asyncIncr(key, by);
    }

    /**
     * 异步Decr方法, 不支持默认值, 若key不存在返回-1.
     */
    public static Future<Long> asyncDecr(String key, int by) {
        return getInstance().asyncDecr(key, by);
    }

    public static void main(String[] args) {
        String key = "key02";
        logger.info(get(key));
    }

}
