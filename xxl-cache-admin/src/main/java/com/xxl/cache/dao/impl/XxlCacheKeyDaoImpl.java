package com.xxl.cache.dao.impl;

import com.xxl.cache.core.model.XxlCacheKey;
import com.xxl.cache.dao.IXxlCacheKeyDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
@Component
public class XxlCacheKeyDaoImpl implements IXxlCacheKeyDao {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<XxlCacheKey> pageList(Map<String, Object> params) {
        return sqlSessionTemplate.selectList("XxlCacheKeyMapper.pageList", params);
    }

    @Override
    public int pageListCount(Map<String, Object> params) {
        return sqlSessionTemplate.selectOne("XxlCacheKeyMapper.pageListCount", params);
    }

    @Override
    public int save(XxlCacheKey xxlCacheKey) {
        return sqlSessionTemplate.insert("XxlCacheKeyMapper.save", xxlCacheKey);
    }

    @Override
    public int update(XxlCacheKey xxlCacheKey) {
        return sqlSessionTemplate.update("XxlCacheKeyMapper.update", xxlCacheKey);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlCacheKeyMapper.delete", id);
    }

    @Override
    public XxlCacheKey load(int id) {
        return sqlSessionTemplate.selectOne("XxlCacheKeyMapper.load", id);
    }
}
