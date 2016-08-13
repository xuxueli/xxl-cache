package com.xxl.cache.service.impl;

import com.xxl.cache.core.util.JedisUtil;
import com.xxl.cache.core.util.PropertiesUtil;
import com.xxl.cache.core.util.XMemcachedUtil;
import com.xxl.cache.service.IXxlCacheService;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.xxl.cache.core.util.PropertiesUtil.DEFAULT_CONFIG;

/**
 * Created by xuxueli on 16/8/13.
 */
@Service
public class XxlCacheServiceImpl implements IXxlCacheService {

    /**
     * 系统支持的缓存类型
     */
    public enum CacheTypeEnum {
        Memcached, Redis;
        public static CacheTypeEnum match(String type){
            for (CacheTypeEnum item: CacheTypeEnum.values()) {
                if (item.name().equals(type)) {
                    return item;
                }
            }
            return null;
        }
    }

    /**
     * 当前系统配置生效的缓存类型
     */
    public static final CacheTypeEnum CACHE_TYPE;
    static {
        Properties prop = PropertiesUtil.loadProperties(DEFAULT_CONFIG);
        String cacheTypeStr = PropertiesUtil.getString(prop, "cache.type");
        CacheTypeEnum cacheType = CacheTypeEnum.match(cacheTypeStr);
        CACHE_TYPE = (cacheType!=null) ? cacheType : CacheTypeEnum.Redis;
    }


    /**
     * 查询缓存
     * @param key
     * @return
     */
    public Object get(String key) {
        switch (CACHE_TYPE) {
            case Memcached:
                return XMemcachedUtil.get(key);
            case Redis:
                return JedisUtil.getObjectValue(key);
            default:
                return null;
        }
    }

    /**
     * 清除缓存
     * @param key
     * @return
     */
    public boolean delete(String key) {
        switch (CACHE_TYPE) {
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
