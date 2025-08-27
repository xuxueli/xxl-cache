package com.xxl.cache.core.serialize.impl;

import com.google.gson.Gson;
import com.xxl.cache.core.serialize.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * GsonSerializer
 *
 * @author aruato 2025-08-27
 */
public class GsonSerializer extends Serializer {

    private static final Gson GSON = new Gson();

    @Override
    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new RuntimeException("Cannot serialize null object");
        }
        try {
            String json = GSON.toJson(obj);
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            throw new RuntimeException("Cannot deserialize null byte array");
        }
        try {
            String json = new String(bytes, StandardCharsets.UTF_8);
            return (T) GSON.fromJson(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize object: " + e.getMessage(), e);
        }
    }

}
