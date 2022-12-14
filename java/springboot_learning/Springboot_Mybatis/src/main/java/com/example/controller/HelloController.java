package com.example.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author windows
 */
@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello，Spring Boot!";
    }
}
