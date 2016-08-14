package com.xxl.cache.dao;

import com.xxl.cache.core.model.XxlCacheTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
public interface IXxlCacheTemplateDao {

    public List<XxlCacheTemplate> pageList(Map<String, Object> params);
    public int pageListCount(Map<String, Object> params);

    public int save(XxlCacheTemplate xxlCacheTemplate);
    public int update(XxlCacheTemplate xxlCacheTemplate);
    public int delete(int id);

    public XxlCacheTemplate load(int id);

}
