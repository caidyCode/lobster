package com.xc.lobster.service.test.impl;

import com.xc.lobster.client.TestClient;
import com.xc.lobster.mybatis.aspect.DataSourceAnno;
import com.xc.lobster.mybatis.datasource.DataSourceType;
import com.xc.lobster.mybatis.mapper.UserMapper;
import com.xc.lobster.mybatis.model.User;
import com.xc.lobster.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TestClient testClient;

    @DataSourceAnno(value= DataSourceType.MASTER)
    @Override
    public String test() {
        User user = userMapper.selectById(31);
        System.out.println(user);
        testClient.test();
        return "hello world";
    }
}
