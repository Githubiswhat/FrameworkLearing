package com.example.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class DubboConsumerService {


    @DubboReference
    private DemoService demoService;

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

}
