package com.xc.lobster.client.http.model;

import com.xc.lobster.client.http.serializer.FastJsonSerializer;
import com.xc.lobster.client.http.serializer.Serializer;

public enum SerializeMethod {

    /** fastjson */
    FASTJSON_SERIALIZER(FastJsonSerializer.getInstance());

    private Serializer serializer;

    private SerializeMethod(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Getter method for property <tt>serializer</tt>.
     *
     * @return property value of serializer
     */
    public Serializer getSerializer() {
        return serializer;
    }
}
