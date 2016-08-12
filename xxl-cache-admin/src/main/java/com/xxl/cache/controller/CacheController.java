package com.xxl.cache.controller;

import com.xxl.cache.controller.annotation.PermessionLimit;
import com.xxl.cache.core.model.XxlCacheKey;
import com.xxl.cache.core.util.CacheKeyUtil;
import com.xxl.cache.core.util.ReturnT;
import com.xxl.cache.core.util.cache.CacheUtil;
import com.xxl.cache.service.IXxlCacheKeyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/6.
 */
@Controller
@RequestMapping("/cache")
public class CacheController {

    @Resource
    private IXxlCacheKeyService xxlCacheKeyService;


    @RequestMapping("")
    @PermessionLimit
    public String toLogin(Model model) {
        model.addAttribute("CACHE_ENUM", CacheUtil.CACHE_ENUM);
        return "cache/cache.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
        @RequestParam(required = false, defaultValue = "10") int length, String key) {
        return xxlCacheKeyService.pageList(start, length, key);
    }

    @RequestMapping("/save")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> save(XxlCacheKey xxlCacheKey) {
        return xxlCacheKeyService.save(xxlCacheKey);
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> update(XxlCacheKey xxlCacheKey) {
        return xxlCacheKeyService.update(xxlCacheKey);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> delete(int id) {
        return xxlCacheKeyService.delete(id);
    }


    @RequestMapping("/getCacheInfo")
    @ResponseBody
    @PermessionLimit
    public ReturnT<Map<String, Object>> getCacheInfo(String finalKey) {
        return xxlCacheKeyService.getCacheInfo(finalKey);
    }

    @RequestMapping("/removeCache")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> removeCache(String finalKey) {
        return xxlCacheKeyService.removeCache(finalKey);
    }

    @RequestMapping("/getFinalKey")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> getFinalKey(String key, String params) {
        String finalKey = CacheKeyUtil.getFinalKey(key, params);
        return new ReturnT<String>(finalKey);
    }

}
