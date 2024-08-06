package com.twentyone.steachserver.domain.curriculum.service.redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurriculumRedisService {
    private static final String LATEST_CURRICULUMS_KEY = "latestCurriculums";
    private static final String POPULAR_RATIO_CURRICULUMS_KEY = "popularRatioCurriculums";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public CurriculumRedisService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }


    public List<CurriculumDetailResponse> getPopularRatioCurriculum() {
        return getCurriculumDetailResponses(POPULAR_RATIO_CURRICULUMS_KEY);
    }

    public List<CurriculumDetailResponse> getLatestCurricula() {
        return getCurriculumDetailResponses(LATEST_CURRICULUMS_KEY);
    }

    private List<CurriculumDetailResponse> getCurriculumDetailResponses(String latestCurriculumsKey) {
        String json = redisTemplate.opsForValue().get(latestCurriculumsKey);
        if (json != null) {
            try {
                return objectMapper.readValue(json, new TypeReference<List<CurriculumDetailResponse>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert JSON to list", e);
            }
        }
        return null;
    }

    /**
     * 일반적인 경우: 데이터를 JSON으로 변환하여 저장하는 방법이 더 효율적일 가능성이 높습니다. 이는 한 번의 SET 연산으로 모든 데이터를 저장할 수 있기 때문입니다.
     * 특수한 경우: 데이터의 크기가 매우 크거나 변환 비용이 매우 큰 경우에는 기존 데이터를 삭제하고 새 데이터를 추가하는 방법이 더 효율적일 수 있습니다.
     * @param list
     */
    public void savePopularRatioCurricula(List<CurriculumDetailResponse> list) {
        try {
            String json = objectMapper.writeValueAsString(list);
            redisTemplate.opsForValue().set(POPULAR_RATIO_CURRICULUMS_KEY, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list to JSON", e);
        }
    }

    public void saveLatestCurricula(List<CurriculumDetailResponse> list) {
        try {
            String json = objectMapper.writeValueAsString(list);
            redisTemplate.opsForValue().set(LATEST_CURRICULUMS_KEY, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list to JSON", e);
        }
    }


    public void addLatestCurriculum(CurriculumDetailResponse curriculum) {
        List<CurriculumDetailResponse> currentList = getLatestCurricula();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        if (currentList.size() >= 7) { // 7개 유지
            currentList.remove(currentList.size() - 1); // 마지막 항목 제거
        }
        currentList.add(0, curriculum); // 첫 번째에 추가

        try {
            String json = objectMapper.writeValueAsString(currentList);
            redisTemplate.opsForValue().set(LATEST_CURRICULUMS_KEY, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list to JSON", e);
        }
    }
}
