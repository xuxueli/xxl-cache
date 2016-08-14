package com.xxl.cache.core.dto;

import java.text.MessageFormat;

/**
 * Created by xuxueli on 16/8/13.
 */
public class XxlCacheKey {

    private String key;
    private String[] params;

    public XxlCacheKey(String key, String... params) {
        this.key = key;
        this.params = params;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getFinalKey() {
        return MessageFormat.format(key, params);
    }

}
