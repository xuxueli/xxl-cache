package com.xxl.cache.service;

import com.xxl.cache.core.model.XxlCacheKey;
import com.xxl.cache.core.util.ReturnT;

import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
public interface IXxlCacheKeyService {

    public Map<String,Object> pageList(int offset, int pagesize, String key);

    public ReturnT<String> save(XxlCacheKey xxlCacheKey);

    public ReturnT<String> update(XxlCacheKey xxlCacheKey);

    public ReturnT<String> delete(int id);

    public ReturnT<Map<String, Object>> getCacheInfo(String finalKey);

    public ReturnT<String> removeCache(String finalKey);
}
