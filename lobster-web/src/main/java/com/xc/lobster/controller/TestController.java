package com.xc.lobster.controller;

import com.xc.lobster.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService test;

    @RequestMapping("/demo")
    @ResponseBody
    public String testMethod(){
        return test.test();
    }
}
