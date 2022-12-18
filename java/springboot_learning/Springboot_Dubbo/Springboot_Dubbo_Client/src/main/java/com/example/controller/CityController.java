package com.example.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author windows
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CityController {

    private final CityDubboConsumerService cityService;

    @GetMapping("city")
    public String city() {
        return cityService.printCity();
    }
}
