package com.xxl.cache.service.impl;

import com.xxl.cache.core.api.XxlCacheService;
import com.xxl.cache.core.dto.XxlCacheKey;
import com.xxl.cache.core.util.JedisUtil;
import com.xxl.cache.core.util.PropertiesUtil;
import com.xxl.cache.core.util.XMemcachedUtil;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.xxl.cache.core.util.PropertiesUtil.DEFAULT_CONFIG;

/**
 * Created by xuxueli on 16/8/13.
 */
@Service
public class XxlCacheServiceImpl implements XxlCacheService {

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

    @Override
    public boolean set(XxlCacheKey xxlCacheKey, Object value) {
        // 针对缓存管理系统,暂时并不需要Set类型方法, 如若将本Service抽象成公共RPC服务, 可自行完善扩充
        return false;
    }

    /**
     * 查询缓存
     * @param xxlCacheKey
     * @return
     */
    public Object get(XxlCacheKey xxlCacheKey) {
        switch (CACHE_TYPE) {
            case Memcached:
                return XMemcachedUtil.get(xxlCacheKey.getFinalKey());
            case Redis:
                return JedisUtil.getObjectValue(xxlCacheKey.getFinalKey());
            default:
                return null;
        }
    }

    /**
     * 清除缓存
     * @param xxlCacheKey
     * @return
     */
    public boolean delete(XxlCacheKey xxlCacheKey) {
        switch (CACHE_TYPE) {
            case Memcached:
                return XMemcachedUtil.delete(xxlCacheKey.getFinalKey());
            case Redis:
                Long ret = JedisUtil.del(xxlCacheKey.getFinalKey());
                return (ret!=null&&ret>0)?true:false;
            default:
                return false;
        }
    }

}
