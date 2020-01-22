package com.xc.lobster.client.http.model;

import java.util.HashMap;
import java.util.Map;

public class XcHttpResponse<T> {

    private T response;

    /** Http头信息*/
    private Map<String, String> headers = new HashMap<>();

    public XcHttpResponse() {

    }

    public XcHttpResponse(T response, Map<String, String> headers) {
        super();
        this.response = response;
        this.headers = headers;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
