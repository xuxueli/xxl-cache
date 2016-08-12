package com.xxl.cache.dao;

import com.xxl.cache.core.model.XxlCacheKey;

import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
public interface IXxlCacheKeyDao {

    public List<XxlCacheKey> pageList(Map<String, Object> params);
    public int pageListCount(Map<String, Object> params);

    public int save(XxlCacheKey xxlCacheKey);
    public int update(XxlCacheKey xxlCacheKey);
    public int delete(int id);

    public XxlCacheKey load(int id);

}
