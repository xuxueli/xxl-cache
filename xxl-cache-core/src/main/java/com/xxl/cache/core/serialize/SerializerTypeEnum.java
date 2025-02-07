package com.xxl.cache.core.serialize;

import com.xxl.cache.core.serialize.impl.JavaSerializer;

/**
 * serializer
 *
 * @author xuxueli 2025-02-07
 */
public enum SerializerTypeEnum {

    JAVA("java", new JavaSerializer());

    private String type;
    private Serializer serializer;

    SerializerTypeEnum(String type, Serializer serializer) {
        this.type = type;
        this.serializer = serializer;
    }
    public String getType() {
        return type;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public static SerializerTypeEnum match(String type) {
        for (SerializerTypeEnum item : SerializerTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
