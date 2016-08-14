package com.xxl.cache.service.impl;

import com.xxl.cache.core.model.XxlCacheTemplate;
import com.xxl.cache.core.util.ReturnT;
import com.xxl.cache.dao.IXxlCacheTemplateDao;
import com.xxl.cache.service.IXxlCacheTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
@Service
public class XxlCacheTemplateServiceImpl implements IXxlCacheTemplateService {
    private static Logger logger = LogManager.getLogger();


    @Resource
    private IXxlCacheTemplateDao xxlCacheKeyDao;

    @Override
    public Map<String, Object> pageList(int offset, int pagesize, String key) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("offset", offset);
        params.put("pagesize", pagesize);
        params.put("key", key);

        List<XxlCacheTemplate> data = xxlCacheKeyDao.pageList(params);
        int list_count = xxlCacheKeyDao.pageListCount(params);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("data", data);
        maps.put("recordsTotal", list_count);		// 总记录数
        maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
        return maps;
    }

    @Override
    public ReturnT<String> save(XxlCacheTemplate xxlCacheTemplate) {
        if (StringUtils.isBlank(xxlCacheTemplate.getKey())) {
            return new ReturnT<String>(500, "请输入“缓存Key”");
        }
        if (StringUtils.isBlank(xxlCacheTemplate.getIntro())) {
            return new ReturnT<String>(500, "请输入“简介”");
        }
        xxlCacheKeyDao.save(xxlCacheTemplate);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> update(XxlCacheTemplate xxlCacheTemplate) {
        if (StringUtils.isBlank(xxlCacheTemplate.getKey())) {
            return new ReturnT<String>(500, "请输入“缓存Key”");
        }
        if (StringUtils.isBlank(xxlCacheTemplate.getIntro())) {
            return new ReturnT<String>(500, "请输入“简介”");
        }
        xxlCacheKeyDao.update(xxlCacheTemplate);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> delete(int id) {
        xxlCacheKeyDao.delete(id);
        return ReturnT.SUCCESS;
    }

}
