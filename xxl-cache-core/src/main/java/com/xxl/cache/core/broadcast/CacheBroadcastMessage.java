package com.xxl.cache.core.broadcast;

import java.io.Serializable;

public class CacheBroadcastMessage implements Serializable {
    private static final long serialVersionUID = 42L;

    private String category;
    private String key;

    public CacheBroadcastMessage() {
    }
    public CacheBroadcastMessage(String category, String key) {
        this.category = category;
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "CacheBroadcastMessage{" +
                "category='" + category + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

}
