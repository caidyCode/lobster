package com.xc.lobster.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfig {

    @Value("${http.client.pool.connection.timeToLive}")
    private Integer timeToLive;

    @Value("${http.client.pool.connection.maxTotal}")
    private Integer maxTotal;

    @Value("${http.client.pool.connection.maxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${http.client.pool.connection.connectTimeout}")
    private Integer connectTimeout;

    @Value("${http.client.pool.connection.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${http.client.pool.connection.socketTimeout}")
    private Integer socketTimeout;

    @Value("${http.client.pool.connection.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;

    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);
        //最大连接数
        httpClientConnectionManager.setMaxTotal(maxTotal);
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager httpClientConnectionManager){
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        httpClientBuilder.evictExpiredConnections();
        httpClientBuilder.setDefaultRequestConfig(getRequestConfig(getBuilder()));
        return httpClientBuilder;
    }
    @Bean(name = "httpClient")
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }

    @Bean(name = "requestConfigBuilder")
    public RequestConfig.Builder getBuilder(){
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout);
    }

    @Bean(name = "defaultRequestConfig")
    public RequestConfig getRequestConfig(@Qualifier("requestConfigBuilder") RequestConfig.Builder builder){
        return builder.build();
    }

    @Bean(name = "restTemplate")
    public RestTemplate createRestTemplate(@Qualifier("clientHttpRequestFactory") ClientHttpRequestFactory clientHttpRequestFactory){
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean(name = "clientHttpRequestFactory")
    public ClientHttpRequestFactory getClientHttpRequestFactory(@Qualifier("httpClient") CloseableHttpClient httpClient){
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }




}
