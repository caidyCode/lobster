package com.xc.lobster.client.http.model;

import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class XcHttpRequest<T> {

    /**
     * 请求数据
     */
    private T request;

    /**
     * 请求地址
     */
    private String endpoint;

    /**
     * http method
     */
    private HttpMethod requestMethod;

    /**
     * 请求体的序列化方法
     */
    private SerializeMethod requestSerializeMethod;

    private SerializeMethod responseSerializeMethod;

    /** URL参数编码字符集 ，默认UTF-8*/
    private String charset = "UTF-8";

    /** Http头信息*/
    private Map<String, String> headers = new HashMap<>();

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(HttpMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public SerializeMethod getRequestSerializeMethod() {
        return requestSerializeMethod;
    }

    public void setRequestSerializeMethod(SerializeMethod requestSerializeMethod) {
        this.requestSerializeMethod = requestSerializeMethod;
    }

    public SerializeMethod getResponseSerializeMethod() {
        return responseSerializeMethod;
    }

    public void setResponseSerializeMethod(SerializeMethod responseSerializeMethod) {
        this.responseSerializeMethod = responseSerializeMethod;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }


}
