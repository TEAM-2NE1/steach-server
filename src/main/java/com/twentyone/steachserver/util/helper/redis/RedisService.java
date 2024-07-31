package com.twentyone.steachserver.util.helper.redis;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveList(String key, List<Object> list) {
        redisTemplate.opsForValue().set(key, list);
    }

    public void save(Map<String, Object> keyValue) {
        for (Map.Entry<String, Object> stringObjectEntry : keyValue.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public Object find(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @SuppressWarnings("unchecked")
    public List<Object> findList(String key) {
        return (List<Object>) redisTemplate.opsForValue().get(key);
    }
}