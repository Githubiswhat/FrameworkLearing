package com.example;

import com.example.config.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootTestApplicationTests {

    @Autowired
    private RedisCache redisCache;

    @Test
    void contextLoads() {
    }

    @Test
    void TextRedisXX() {
        redisCache.setCacheObject("hhh", "123");
        String str = redisCache.getCacheObject("hhh");
        System.out.println(str);
    }

}
