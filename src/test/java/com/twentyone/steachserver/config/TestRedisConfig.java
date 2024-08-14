package com.twentyone.steachserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;
import java.time.Duration;

@Slf4j
@Profile("test")
@TestConfiguration
public class TestRedisConfig {
    @Value("${spring.data.redis.url}")
    private String url;

    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 키를 문자열로 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 값을 JSON 형식으로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash를 사용할 경우 시리얼라이저를 설정합니다. (문자열로 직렬화)
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        URI redisUri = URI.create(url);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
                redisUri.getHost(),
                redisUri.getPort()
        );

        if (redisUri.getUserInfo() != null) {
            String[] userInfo = redisUri.getUserInfo().split(":");
            if (userInfo.length > 0) {
                config.setUsername(userInfo[0]);
                if (userInfo.length > 1) {
                    config.setPassword(userInfo[1]);
                }
            }
        }

        String path = redisUri.getPath();
        if (path != null && !path.isEmpty()) {
            config.setDatabase(Integer.parseInt(path.substring(1)));
        }

        return new LettuceConnectionFactory(config);
    }

    // CacheManager를 빈으로 정의하는 메서드입니다.
    @Bean
    public CacheManager rcm(RedisConnectionFactory cf) {
        // Redis 캐시 구성 설정을 정의합니다.
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 키를 직렬화하는 방법을 설정합니다. (문자열로 직렬화)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 값을 직렬화하는 방법을 설정합니다. (제네릭 Jackson JSON 직렬화)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                // 캐시 항목의 기본 TTL(수명)을 3분으로 설정합니다.
                .entryTtl(Duration.ofMinutes(3L));

        // RedisCacheManager를 구성하여 반환합니다.
        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(cf)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
