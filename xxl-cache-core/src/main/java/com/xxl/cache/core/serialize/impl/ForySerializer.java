package com.xxl.cache.core.serialize.impl;

import com.xxl.cache.core.broadcast.CacheBroadcastMessage;
import com.xxl.cache.core.serialize.Serializer;
import org.apache.fory.Fory;
import org.apache.fory.config.Language;

public class ForySerializer extends Serializer {
    private Fory fory;
    public ForySerializer() {
        fory=Fory.builder().withLanguage(Language.JAVA)
                 .requireClassRegistration(false)
                .build();
        fory.register(CacheBroadcastMessage.class);

    }

    @Override
    public <T> byte[] serialize(T obj) {
//        fory.register(obj.getClass());
        return fory.serialize(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        return (T)fory.deserialize(bytes);
    }
}
