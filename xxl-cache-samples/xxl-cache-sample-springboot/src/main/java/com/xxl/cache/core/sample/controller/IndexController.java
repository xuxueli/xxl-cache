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

    @RequestMapping("")
    @ResponseBody
    public String index(){
        String result = "hello world.";
        return result;
    }

    @RequestMapping("/set")
    @ResponseBody
    public String set(@RequestParam String value) {
        String category = "user";
        String key = "user03";

        XxlCacheHelper.getCache(category).set(key, value);
        return "Set successfully";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Object get() {
        String category = "user";
        String key = "user03";

        String value = (String) XxlCacheHelper.getCache(category).get(key);
        return "key: " + key + "<br> value: " + value;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete() {
        String category = "user";
        String key = "user03";

        XxlCacheHelper.getCache(category).del(key);
        return "Deleted successfully";
    }

}
