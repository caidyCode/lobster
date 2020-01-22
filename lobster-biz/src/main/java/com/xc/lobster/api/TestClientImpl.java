package com.xc.lobster.api;

import com.alibaba.fastjson.JSONObject;
import com.xc.lobster.client.TestClient;
import com.xc.lobster.client.http.SpringRestTemplate;
import com.xc.lobster.client.http.model.SerializeMethod;
import com.xc.lobster.client.http.model.XcHttpRequest;
import com.xc.lobster.client.http.model.XcHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class TestClientImpl implements TestClient {

    @Autowired
    private SpringRestTemplate springRestTemplate;
    @Override
    public String test() {
        XcHttpResponse<String> response = springRestTemplate.invoke(assembleRequest("1"),String.class);
        System.out.println(response);
        return null;
    }

    private XcHttpRequest<String> assembleRequest(String data){
        XcHttpRequest<String> param = new XcHttpRequest<>();
        param.setEndpoint("http://wmstest.xianchengkeji.cn/index.php//api/supplier/get_supplier_info/");
        param.setRequest(data);
        param.setRequestMethod(HttpMethod.POST);
        param.setRequestSerializeMethod(SerializeMethod.FASTJSON_SERIALIZER);
        param.setRequestSerializeMethod(SerializeMethod.FASTJSON_SERIALIZER);
        param.addHeader("Content-Type", "application/json;charset=UTF-8");
        return param;
    }


}
