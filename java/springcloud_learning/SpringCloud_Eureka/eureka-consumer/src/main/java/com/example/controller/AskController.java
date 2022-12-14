package com.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@Configuration
@RequiredArgsConstructor
public class AskController {

    private final RestTemplate restTemplate;
    @Value("${spring.application.name}")
    private String name;

    @RequestMapping(value = "/ask")
    public String ask() {
        String askHelloFromService = restTemplate.getForEntity("http://EUREKA-PRODUCTOR/hello/{name}", String.class, name).getBody();
        return askHelloFromService;
    }
}
