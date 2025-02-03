package com.xxl.cache.core.util;

import java.io.*;

public class SerializationUtil {

    /**
     * serialize
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Object obj) {
        if (obj == null) {
            throw new RuntimeException("Cannot serialize null object");
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object: " + e.getMessage(), e);
        }
    }

    /**
     * deserialize
     *
     * @param bytes
     * @return
     * @param <T>
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T deserialize(byte[] bytes) {
        if (bytes == null) {
            throw new RuntimeException("Cannot deserialize null byte array");
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize object: " + e.getMessage(), e);
        }
    }
}