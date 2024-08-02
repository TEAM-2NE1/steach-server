package com.twentyone.steachserver.unit.domain.curriculum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.SteachTest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.service.redis.CurriculumRedisService;
import com.twentyone.steachserver.util.converter.DateTimeUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("커리큘럼 실제 레디스 서비스 단위 테스트")
class CurriculumRealRedisServiceTest extends SteachTest {

    private static final String LATEST_CURRICULUMS_KEY = "latestCurriculums";
    private static final String POPULAR_RATIO_CURRICULUMS_KEY = "popularRatioCurriculums";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurriculumRedisService curriculumRedisService;

    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        assertNotNull(redisTemplate);
        valueOperations = redisTemplate.opsForValue();
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    static class TypeReferenceCapture extends TypeReference<List<CurriculumDetailResponse>> {}

    static class TypeReferenceCaptureMatcher implements org.mockito.ArgumentMatcher<TypeReference<List<CurriculumDetailResponse>>> {
        private final TypeReference<List<CurriculumDetailResponse>> expectedTypeReference;

        public TypeReferenceCaptureMatcher(TypeReference<List<CurriculumDetailResponse>> expectedTypeReference) {
            this.expectedTypeReference = expectedTypeReference;
        }

        @Override
        public boolean matches(TypeReference<List<CurriculumDetailResponse>> argument) {
            return argument.getType().equals(expectedTypeReference.getType());
        }
    }

    private CurriculumDetailResponse createMockCurriculumDetailResponse(int id) {
        return CurriculumDetailResponse.builder()
                .curriculumId(id)
                .teacherName("Teacher " + id)
                .title("Title " + id)
                .subTitle("Subtitle " + id)
                .intro("Intro " + id)
                .information("Information " + id)
                .category(CurriculumCategory.EDUCATION)
                .subCategory("SubCategory " + id)
                .bannerImgUrl("BannerUrl " + id)
                .startDate(DateTimeUtil.convert(LocalDateTime.now()))
                .endDate(DateTimeUtil.convert(LocalDateTime.now().plusDays(10)))
                .weekdaysBitmask("1110000")
                .lectureStartTime("10:00")
                .lectureEndTime("12:00")
                .currentAttendees(10)
                .maxAttendees(30)
                .createdAt(DateTimeUtil.convert(LocalDateTime.now()))
                .build();
    }

    @Test
    @DisplayName("레디스 기본 작동 테스트")
    void testRedis() {
        // given
        valueOperations.set("key", "value");

        // when
        String value = valueOperations.get("key");

        // then
        assertEquals("value", value);
    }

    @Test
    @DisplayName("인기 있는 커리큘럼 조회 테스트")
    void testGetPopularRatioCurriculum() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        String mockJson = objectMapper.writeValueAsString(mockList);

        // when
        valueOperations.set(POPULAR_RATIO_CURRICULUMS_KEY, mockJson);
        String redisData = valueOperations.get(POPULAR_RATIO_CURRICULUMS_KEY);
        List<CurriculumDetailResponse> result = objectMapper.readValue(redisData, new TypeReference<List<CurriculumDetailResponse>>() {});

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Teacher 1", result.get(0).getTeacherName());
    }

    @Test
    @DisplayName("최신 커리큘럼 조회 테스트")
    void testGetLatestCurricula() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        String mockJson = objectMapper.writeValueAsString(mockList);

        // when
        valueOperations.set(LATEST_CURRICULUMS_KEY, mockJson);
        String redisData = valueOperations.get(LATEST_CURRICULUMS_KEY);
        List<CurriculumDetailResponse> result = objectMapper.readValue(redisData, new TypeReference<List<CurriculumDetailResponse>>() {});

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Teacher 1", result.get(0).getTeacherName());
    }

    @Test
    @DisplayName("인기 있는 커리큘럼 저장 테스트")
    void testSavePopularRatioCurricula() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        String mockJson = objectMapper.writeValueAsString(mockList);

        // when
        curriculumRedisService.savePopularRatioCurricula(mockList);

        // then
        assertEquals(mockJson, valueOperations.get(POPULAR_RATIO_CURRICULUMS_KEY));
    }

    @Test
    @DisplayName("최신 커리큘럼 추가 테스트")
    void testAddLatestCurriculum() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mockList.add(createMockCurriculumDetailResponse(i));
        }
        String mockJson = objectMapper.writeValueAsString(mockList);
        CurriculumDetailResponse newCurriculum = createMockCurriculumDetailResponse(7);

        // when
        valueOperations.set(LATEST_CURRICULUMS_KEY, mockJson);
        String redisData = valueOperations.get(LATEST_CURRICULUMS_KEY);
        List<CurriculumDetailResponse> currentCurricula = objectMapper.readValue(redisData, new TypeReference<List<CurriculumDetailResponse>>() {});
        currentCurricula.add(newCurriculum);
        valueOperations.set(LATEST_CURRICULUMS_KEY, objectMapper.writeValueAsString(currentCurricula));

        // then
        List<CurriculumDetailResponse> result = objectMapper.readValue(valueOperations.get(LATEST_CURRICULUMS_KEY), new TypeReference<List<CurriculumDetailResponse>>() {});
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(curriculum -> curriculum.getCurriculumId() == 7));
    }

    @Test
    @DisplayName("최신 커리큘럼 저장 테스트")
    void testSaveLatestCurricula() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        String mockJson = objectMapper.writeValueAsString(mockList);

        // when
        curriculumRedisService.saveLatestCurricula(mockList);

        // then
        assertEquals(mockJson, valueOperations.get(LATEST_CURRICULUMS_KEY));
    }

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
