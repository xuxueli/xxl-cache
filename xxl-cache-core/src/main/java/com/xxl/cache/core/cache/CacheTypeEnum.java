package com.xxl.cache.core.cache;

public enum CacheTypeEnum {
    REDIS("redis"),
    CAFFEINE("caffeine");

    private String type;

    CacheTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static CacheTypeEnum match(String name) {
        for (CacheTypeEnum cacheTypeEnum : CacheTypeEnum.values()) {
            if (cacheTypeEnum.getType().equals(name)) {
                return cacheTypeEnum;
            }
        }
        return null;
    }

}
