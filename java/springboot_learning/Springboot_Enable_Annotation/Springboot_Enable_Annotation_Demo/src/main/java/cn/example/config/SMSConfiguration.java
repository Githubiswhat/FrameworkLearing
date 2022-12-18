package cn.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "sms")
@Configuration
@Data
public class SMSConfiguration {

    private final List<String> types = new ArrayList<>();
    private int retryLimitationMinutes;
    private int validityMinute;

}
