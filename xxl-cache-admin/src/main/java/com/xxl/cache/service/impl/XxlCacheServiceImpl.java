package com.xxl.cache.service.impl;

import com.xxl.cache.core.util.JedisUtil;
import com.xxl.cache.core.util.PropertiesUtil;
import com.xxl.cache.core.util.XMemcachedUtil;
import com.xxl.cache.service.IXxlCacheService;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.xxl.cache.core.util.PropertiesUtil.DEFAULT_CONFIG;

/**
 * 推荐将缓存Service抽象成公共RPC服务, 有以下好处:
 *      1、统一监控和维护缓存服务;
 *      2、方便控制client连接数量;
 *      3、缓存节点变更更加方便;
 *      4、在节点变更时, 缓存分片很大可能会受影响, 这将导致不同服务的分片逻辑出现不一致的情况, 统一缓存服务可以避免之;
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
