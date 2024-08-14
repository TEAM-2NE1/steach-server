package com.twentyone.steachserver.unit;

import com.twentyone.steachserver.SteachTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("Redis 설정 정보 테스트")
class RedisConfigInfoTest extends SteachTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Environment env;

    @BeforeEach
    void setUp() {
        assertNotNull(redisTemplate);
    }

    @Test
    @DisplayName("Redis URL 출력 테스트")
    void testRedisUrl() {
        String redisUrl = env.getProperty("spring.data.redis.url");
        assertNotNull(redisUrl);
    }
}
