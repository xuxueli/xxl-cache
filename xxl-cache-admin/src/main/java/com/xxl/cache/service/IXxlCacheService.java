package com.xxl.cache.service;

/**
 * Created by xuxueli on 16/8/13.
 */
public interface IXxlCacheService {

    public Object get(String key) ;

    public boolean delete(String key);

}
