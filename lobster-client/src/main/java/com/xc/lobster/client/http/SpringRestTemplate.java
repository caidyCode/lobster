package com.xc.lobster.client.http;

import com.xc.lobster.client.http.model.XcHttpRequest;
import com.xc.lobster.client.http.model.XcHttpResponse;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SpringRestTemplate {

    private static List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    static {
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
    }



    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    public <Request,Response> XcHttpResponse<Response> invoke(XcHttpRequest<Request> request, Class<Response> clazz){
        validate(request);
        HttpHeaders httpHeaders = getHttpHeaders(request.getHeaders());
        String responseEntity = null;
        HttpMethod method = request.getRequestMethod();
        String url = request.getEndpoint();
        if(method == HttpMethod.POST){
            String bodyRequestStr = getBodyParameters(request);
            HttpEntity<String> httpEntity = new HttpEntity<>(bodyRequestStr, httpHeaders);
            ResponseExtractor<String> responseExtractor = getResponseEntity(String.class);
            RequestCallback requestCallback = new HttpEntityRequestCallback(httpEntity, String.class);
            responseEntity = restTemplate.execute(url, method, requestCallback, responseExtractor);
        }
        if(method == HttpMethod.GET){

        }
        XcHttpResponse<Response> response = createResponse(request, responseEntity, clazz);
        return response;
    }

    /**
     * 入参校验
     *
     * @param request
     */
    private <Request> void validate(XcHttpRequest<Request> request) {
        Assert.notNull(request, "Request is invalid");
        Assert.notNull(request.getRequest(), "Request parameter is required");
        Assert.isTrue(!StringUtils.isEmpty(request.getEndpoint()), "Endpoint url is required");
        Assert.notNull(request.getRequestMethod(), "Http request method is required");
    }

    private HttpHeaders getHttpHeaders(Map<String, String> httpHeaders) {
        HttpHeaders hh = new HttpHeaders();
        for (Map.Entry<String, String> header : httpHeaders.entrySet()) {
            hh.add(header.getKey(), header.getValue());
        }
        return hh;
    }

    /**
     * 构建post body请求参数
     *
     * @param request
     * @return
     */
    private <Request> String getBodyParameters(XcHttpRequest<Request> request) {
        Assert.notNull(request.getRequestSerializeMethod(), "Request serializer is invalid");
        String requestParamStr = request.getRequestSerializeMethod().getSerializer().serialize(request.getRequest(),null);
        return requestParamStr;
    }

    /**
     * 生成响应处理器
     *
     * @param type
     * @return
     */
    private ResponseExtractor<String> getResponseEntity(Type type) {
        ResponseExtractor<String> responseExtractor = new HttpMessageConverterExtractor<>(type, messageConverters);
        return responseExtractor;
    }

    /**
     * 创建response
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    private <Request, Response> XcHttpResponse<Response> createResponse(XcHttpRequest<Request> request,String body, Class<Response> clazz) {
        String responseBodyStr = body;

        if (responseBodyStr == null) {
            return null;
        }

        XcHttpResponse<Response> response = new XcHttpResponse<Response>();
        if (request.getResponseSerializeMethod() == null) {
            response.setResponse((Response) responseBodyStr);
        } else {
            Response responseObj = request.getResponseSerializeMethod().getSerializer().deserialize(responseBodyStr, clazz, null);
            response.setResponse(responseObj);
        }
        //response.getHeaders().putAll(responseEntity.getHeaders().toSingleValueMap());
        return response;
    }

    private class AcceptHeaderRequestCallback implements RequestCallback {

        private final Type responseType;

        private AcceptHeaderRequestCallback(Type responseType) {
            this.responseType = responseType;
        }

        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (responseType != null) {
                Class<?> responseClass = null;
                if (responseType instanceof Class) {
                    responseClass = (Class<?>) responseType;
                }

                List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
                for (HttpMessageConverter<?> converter : messageConverters) {
                    if (responseClass != null) {
                        if (converter.canRead(responseClass, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }
                    else if (converter instanceof GenericHttpMessageConverter) {

                        GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                        if (genericConverter.canRead(responseType, null, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }

                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    MediaType.sortBySpecificity(allSupportedMediaTypes);
                    request.getHeaders().setAccept(allSupportedMediaTypes);
                }
            }
        }

        private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
            List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
            List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
            for (MediaType supportedMediaType : supportedMediaTypes) {
                if (supportedMediaType.getCharset() != null) {
                    supportedMediaType =
                            new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
                }
                result.add(supportedMediaType);
            }
            return result;
        }
    }

    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

        private final HttpEntity<?> requestEntity;

        private HttpEntityRequestCallback(Object requestBody) {
            this(requestBody, null);
        }

        private HttpEntityRequestCallback(Object requestBody, Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity<?>) requestBody;
            } else if (requestBody != null) {
                this.requestEntity = new HttpEntity<Object>(requestBody);
            } else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            if (!requestEntity.hasBody()) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    httpHeaders.putAll(requestHeaders);
                }
                if (httpHeaders.getContentLength() == -1) {
                    httpHeaders.setContentLength(0L);
                }
            } else {
                Object requestBody = requestEntity.getBody();
                Class<?> requestType = requestBody.getClass();
                HttpHeaders requestHeaders = requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter<?> messageConverter : messageConverters) {
                    if (messageConverter.canWrite(requestType, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            httpRequest.getHeaders().putAll(requestHeaders);
                        }
                        ((HttpMessageConverter<Object>) messageConverter).write(requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "Could not write request: no suitable HttpMessageConverter found for request type [" + requestType.getName() + "]";
                if (requestContentType != null) {
                    message += " and content type [" + requestContentType + "]";
                }
                throw new RestClientException(message);
            }
        }
    }

}
