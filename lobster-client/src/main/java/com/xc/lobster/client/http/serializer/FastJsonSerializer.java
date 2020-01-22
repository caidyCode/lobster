package com.xc.lobster.client.http.serializer;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

public class FastJsonSerializer implements Serializer{

    private FastJsonSerializer() {
    }

    public static final FastJsonSerializer getInstance() {
        return FastJsonSerializerHolder.INSTANCE;
    }

    @Override
    public <T> String serialize(T obj, String dateFormat) {
        if (StringUtils.isEmpty(dateFormat)) {
            return JSON.toJSONString(obj);
        } else {
            return JSON.toJSONStringWithDateFormat(obj, dateFormat);
        }
    }

    @Override
    public <T> T deserialize(String text, Class<T> clazz, String dateFormat) {
        return JSON.parseObject(text,clazz);
    }

    private static final class FastJsonSerializerHolder {
        private static final FastJsonSerializer INSTANCE = new FastJsonSerializer();
    }
}
