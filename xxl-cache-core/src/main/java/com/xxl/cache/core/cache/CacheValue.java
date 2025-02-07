package com.xxl.cache.core.cache;

import java.io.Serializable;

/**
 * cache value
 *
 * @author xuxueli 2025-02-04
 */
public class CacheValue implements Serializable {
    private static final long serialVersionUID = 42L;

    private static final long NONE_EXPIRATION_PERIOD = 1000L * 60 * 60 * 24 * 365 * 99;

    /**
     * cache value
     */
    private Object value;

    /**
     * survival time length, milliseconds. Expires after the specified time from the current
     */
    private long survivalTime;

    /**
     * expiration time point, milliseconds
     */
    private long expirationTime;

    public CacheValue() {
    }

    public CacheValue(Object value) {
        this.value = value;
        this.survivalTime = NONE_EXPIRATION_PERIOD;
        this.expirationTime = System.currentTimeMillis() + this.survivalTime;
    }

    public CacheValue(Object value, long survivalTime) {
        this.value = value;
        this.survivalTime = survivalTime>0?survivalTime:NONE_EXPIRATION_PERIOD;
        this.expirationTime = System.currentTimeMillis() + this.survivalTime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getSurvivalTime() {
        return survivalTime;
    }

    public void setSurvivalTime(long survivalTime) {
        this.survivalTime = survivalTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "CacheValue{" +
                "value=" + value +
                ", survivalTime=" + survivalTime +
                ", expirationTime=" + expirationTime +
                '}';
    }

    // tool
    /**
     * isValid
     */
    public boolean isValid() {
        return System.currentTimeMillis() < expirationTime;
    }

}
