package com.xc.lobster.client.http.serializer;

public interface Serializer {

    /**
     * 对象序列化
     *
     * @param obj
     * @param dateFormat
     * @return
     */
    <T> String serialize(T obj, String dateFormat);

    /**
     * 对象反序列化
     *
     * @param text
     * @param clazz
     * @param dateFormat
     * @return
     */
    <T> T deserialize(String text, Class<T> clazz, String dateFormat);

}
