package com.xxl.cache.dao.impl;

import com.xxl.cache.core.model.XxlCacheTemplate;
import com.xxl.cache.dao.IXxlCacheTemplateDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
@Component
public class XxlCacheKeyTemplateImpl implements IXxlCacheTemplateDao {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<XxlCacheTemplate> pageList(Map<String, Object> params) {
        return sqlSessionTemplate.selectList("XxlCacheTemplateMapper.pageList", params);
    }

    @Override
    public int pageListCount(Map<String, Object> params) {
        return sqlSessionTemplate.selectOne("XxlCacheTemplateMapper.pageListCount", params);
    }

    @Override
    public int save(XxlCacheTemplate xxlCacheTemplate) {
        return sqlSessionTemplate.insert("XxlCacheTemplateMapper.save", xxlCacheTemplate);
    }

    @Override
    public int update(XxlCacheTemplate xxlCacheTemplate) {
        return sqlSessionTemplate.update("XxlCacheTemplateMapper.update", xxlCacheTemplate);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlCacheTemplateMapper.delete", id);
    }

    @Override
    public XxlCacheTemplate load(int id) {
        return sqlSessionTemplate.selectOne("XxlCacheTemplateMapper.load", id);
    }
}
