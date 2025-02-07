package com.xxl.cache.core.sample.controller;

import com.xxl.cache.core.XxlCacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class IndexController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 1、定义缓存对象，并指定 “缓存category + 过期时间”
     */
    private XxlCacheHelper.XxlCache userCache = XxlCacheHelper.getCache("user", 60*1000);

    @RequestMapping("")
    @ResponseBody
    public String index(){
        String key = "user03";

        /**
         * 2、缓存读：按照 L1 -> L2 顺序依次读取缓存，如果L1存在缓存则返回，否则读取L2缓存并同步L1；
         */
        String value = userCache.get(key);
        return "key: " + key + "<br> value: " + value;
    }

    @RequestMapping("/set")
    @ResponseBody
    public String set(@RequestParam String value) {
        String key = "user03";

        /**
         * 3、缓存写：按照 L1 -> L2 顺序依次写缓存，同时借助内部广播机制更新全局L1节点缓存；
         */
        userCache.set(key, value);
        return "Set successfully";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete() {
        String key = "user03";

        /**
         * 4、缓存删：按照 L1 -> L2 顺序依次删缓存，同时借助内部广播机制更新全局L1节点缓存；
         */
        userCache.del(key);
        return "Deleted successfully";
    }

}
