package com.twentyone.steachserver.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedis() {
        String key = "testKey";
        String value = "testValue";

        redisService.save(key, value);
        Object retrievedValue = redisService.find(key);

        assertThat(retrievedValue).isEqualTo(value);
    }
}