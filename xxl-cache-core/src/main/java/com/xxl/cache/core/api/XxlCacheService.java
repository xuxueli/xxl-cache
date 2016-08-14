package com.xxl.cache.core.api;

import com.xxl.cache.core.dto.XxlCacheKey;

/**
 * 公共缓存服务, 推荐将该服务抽象成公共RPC服务
 * Created by xuxueli on 16/8/13.
 */
public interface XxlCacheService {

    public boolean set(XxlCacheKey xxlCacheKey, Object value);

    public Object get(XxlCacheKey xxlCacheKey);

    public boolean delete(XxlCacheKey xxlCacheKey);

}
