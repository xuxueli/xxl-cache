package com.xxl.cache.service;

import com.xxl.cache.core.model.XxlCacheTemplate;
import com.xxl.cache.core.util.ReturnT;

import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
public interface IXxlCacheTemplateService {

    public Map<String,Object> pageList(int offset, int pagesize, String key);

    public ReturnT<String> save(XxlCacheTemplate xxlCacheTemplate);

    public ReturnT<String> update(XxlCacheTemplate xxlCacheTemplate);

    public ReturnT<String> delete(int id);

}
