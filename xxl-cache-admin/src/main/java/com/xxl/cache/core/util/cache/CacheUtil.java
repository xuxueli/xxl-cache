package com.xxl.cache.core.util.cache;

import com.xxl.cache.core.util.cache.memcached.XMemcachedUtil;
import com.xxl.cache.core.util.cache.redis.JedisUtil;

/**
 * Created by xuxueli on 16/8/9.
 */
public class CacheUtil {

    // 设置缓存类型
    public enum CacheEnum{Memcached, Redis}
    public static final CacheEnum CACHE_ENUM = CacheEnum.Memcached;

    public static Object get(String key) {
        switch (CACHE_ENUM) {
            case Memcached:
                return XMemcachedUtil.get(key);
            case Redis:
                String tmp1 = JedisUtil.getStringValue(key);
                if (tmp1 == null) {
                    return JedisUtil.getObjectValue(key);
                }
                return tmp1;
            default:
                return null;
        }
    }

    public static boolean delete(String key) {
        switch (CACHE_ENUM) {
            case Memcached:
                return XMemcachedUtil.delete(key);
            case Redis:
                Long ret = JedisUtil.del(key);
                return (ret!=null&&ret>0)?true:false;
            default:
                return false;
        }
    }


}
