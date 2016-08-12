package com.xxl.cache.core.util;

import java.text.MessageFormat;

/**
 * Created by xuxueli on 16/8/9.
 */
public class CacheKeyUtil {

    public static String getFinalKey(String key, String params){
        Object[] paramArr = (params!=null&&params.trim().length()>0)?params.split(","):null;
        return getFinalKey(key, paramArr);
    }

    public static String getFinalKey(String key, Object ... params){
        return MessageFormat.format(key, params);
    }

    public static void main(String[] args) {
        String key = "key03_shopid{0}_cityid{1}";
        String params = "666,999";

        System.out.println(getFinalKey(key, params));
        System.out.println(getFinalKey(key, 666, 999));
        System.out.println(getFinalKey(key, 666));
        System.out.println(getFinalKey(key));

    }

}
