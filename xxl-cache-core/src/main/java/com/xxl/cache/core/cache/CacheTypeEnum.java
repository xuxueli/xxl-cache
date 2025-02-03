package com.xxl.cache.core.cache;

public enum CacheTypeEnum {
    REDIS("redis"),
    CAFFEINE("caffeine");

    private String name;

    CacheTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CacheTypeEnum match(String name) {
        for (CacheTypeEnum cacheTypeEnum : CacheTypeEnum.values()) {
            if (cacheTypeEnum.getName().equals(name)) {
                return cacheTypeEnum;
            }
        }
        return null;
    }

}
