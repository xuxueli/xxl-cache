package com.xxl.cache.core.serialize;

import com.xxl.cache.core.serialize.impl.ForySerializer;
import com.xxl.cache.core.serialize.impl.JavaSerializer;

/**
 * serializer
 *
 * @author xuxueli 2025-02-07
 */
public enum SerializerTypeEnum {

    JAVA("java"),
    FORY("fory"),
    ;

    private String type;
//    private Serializer serializer;

    SerializerTypeEnum(String type) {
        this.type = type;
//        this.serializer = serializer;
    }
    public String getType() {
        return type;
    }

//    public Serializer getSerializer() {
//        return serializer;
//    }

    public static SerializerTypeEnum match(String type) {
        for (SerializerTypeEnum item : SerializerTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
    public static Serializer initSerializer(SerializerTypeEnum serializerTypeEnum) {
        if(serializerTypeEnum==SerializerTypeEnum.JAVA){
            return new JavaSerializer();
        }else if(serializerTypeEnum==SerializerTypeEnum.FORY){
            return new ForySerializer();
        }
        return null;
    }
}
