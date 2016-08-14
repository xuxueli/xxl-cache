package com.xxl.cache.controller;

import com.xxl.cache.controller.annotation.PermessionLimit;
import com.xxl.cache.core.api.XxlCacheService;
import com.xxl.cache.core.dto.XxlCacheKey;
import com.xxl.cache.core.model.XxlCacheTemplate;
import com.xxl.cache.core.util.JacksonUtil;
import com.xxl.cache.core.util.ReturnT;
import com.xxl.cache.service.IXxlCacheTemplateService;
import com.xxl.cache.service.impl.XxlCacheServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/6.
 */
@Controller
@RequestMapping("/cache")
public class CacheController {

    @Resource
    private XxlCacheService xxlCacheService;
    @Resource
    private IXxlCacheTemplateService xxlCacheTemplateService;


    @RequestMapping("")
    @PermessionLimit
    public String toLogin(Model model) {
        model.addAttribute("CACHE_ENUM", XxlCacheServiceImpl.CACHE_TYPE);
        return "cache/cache.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
        @RequestParam(required = false, defaultValue = "10") int length, String key) {
        return xxlCacheTemplateService.pageList(start, length, key);
    }

    @RequestMapping("/save")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> save(XxlCacheTemplate xxlCacheKey) {
        return xxlCacheTemplateService.save(xxlCacheKey);
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> update(XxlCacheTemplate xxlCacheKey) {
        return xxlCacheTemplateService.update(xxlCacheKey);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> delete(int id) {
        return xxlCacheTemplateService.delete(id);
    }


    @RequestMapping("/getCacheInfo")
    @ResponseBody
    @PermessionLimit
    public ReturnT<Map<String, Object>> getCacheInfo(String key, String param) {
        String[] params = param!=null?param.split(","):null;
        XxlCacheKey xxlCacheKey = new XxlCacheKey(key, params);

        // 查询缓存
        Object cacheVal = xxlCacheService.get(xxlCacheKey);
        if (cacheVal==null) {
            return new ReturnT(500, "缓存数据为空");
        }

        // 封装返回
        String info = cacheVal.toString();
        String type = cacheVal.getClass().getName();
        int length = 1;
        if (cacheVal instanceof List) {
            length = ((List) cacheVal).size();
        }
        String json = JacksonUtil.writeValueAsString(cacheVal);

        Map<String, Object> cacheInfo = new HashMap<String, Object>();
        cacheInfo.put("finalKey", xxlCacheKey.getFinalKey());
        cacheInfo.put("type", type);
        cacheInfo.put("type", type);
        cacheInfo.put("length", length);
        cacheInfo.put("info", info);
        cacheInfo.put("json", json);

        return new ReturnT<Map<String, Object>>(cacheInfo);
    }

    @RequestMapping("/removeCache")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> removeCache(String key, String param) {
        String[] params = param!=null?param.split(","):null;
        XxlCacheKey xxlCacheKey = new XxlCacheKey(key, params);

        boolean ret = xxlCacheService.delete(xxlCacheKey);
        return ret?ReturnT.SUCCESS:ReturnT.FAIL;
    }

}
