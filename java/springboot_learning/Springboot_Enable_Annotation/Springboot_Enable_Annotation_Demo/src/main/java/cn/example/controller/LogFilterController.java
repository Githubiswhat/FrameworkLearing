package cn.example.controller;

import cn.example.config.SMSConfiguration;
import cn.example.logFilter.starter.annotation.EnableLogFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@EnableLogFilter
public class LogFilterController {

    private final SMSConfiguration smsConfiguration;

    @GetMapping("/test")
    public String test() {
        log.info("smsConfiguration is : {}", smsConfiguration);
        return "this is a demo boot.";
    }
}
