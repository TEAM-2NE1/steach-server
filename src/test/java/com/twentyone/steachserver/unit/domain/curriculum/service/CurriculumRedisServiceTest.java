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
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("커리큘럼 레디스 서비스 단위 테스트")
class CurriculumRedisServiceTest extends SteachTest {

    private static final String LATEST_CURRICULUMS_KEY = "latestCurriculums";
    private static final String POPULAR_RATIO_CURRICULUMS_KEY = "popularRatioCurriculums";

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private CurriculumRedisService curriculumRedisService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
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
                .category(CurriculumCategory.getCategoryByIndex(0))
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
        when(valueOperations.get("key")).thenReturn("value");

        // when
        redisTemplate.opsForValue().set("key", "value");
        String value = redisTemplate.opsForValue().get("key");

        // then
        assertEquals("value", value);
    }

    @Test
    @DisplayName("인기 있는 커리큘럼 조회 테스트")
    void testGetPopularRatioCurriculum() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        ObjectMapper actualObjectMapper = new ObjectMapper();
        String mockJson = actualObjectMapper.writeValueAsString(mockList);

        // when
        when(valueOperations.get(POPULAR_RATIO_CURRICULUMS_KEY)).thenReturn(mockJson);
        when(objectMapper.readValue(eq(mockJson), any(TypeReference.class))).thenReturn(mockList);

        List<CurriculumDetailResponse> result = curriculumRedisService.getPopularRatioCurriculum();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Teacher 1", result.get(0).getTeacherName());
    }

    /**
     * 목킹(Mocking)은 테스트에서 의존하는 외부 시스템이나 객체를 모방하여,
     * 테스트 대상 코드가 기대하는 동작을 수행하도록 설정하는 과정입니다.
     * 이를 통해 실제로 외부 시스템에 의존하지 않고도 테스트를 수행할 수 있습니다.
     * 특히, 외부 시스템과의 상호작용을 모방하여 테스트 대상 코드가 예상대로 동작하는지 검증할 수 있습니다.
     * @throws JsonProcessingException
     */
    @Test
    @DisplayName("최신 커리큘럼 조회 테스트")
    void testGetLatestCurricula() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        // mockList라는 커리큘럼 목록을 생성하고 이를 JSON 문자열로 변환합니다.
        String mockJson = new ObjectMapper().writeValueAsString(mockList);

        // when
        // 메서드가 호출될 때 mockJson을 반환하도록 설정합니다. 이는 Redis에서 데이터를 가져오는 동작을 모킹하는 부분입니다.
        when(valueOperations.get(LATEST_CURRICULUMS_KEY)).thenReturn(mockJson);

        // objectMapper.readValue(mockJson, typeRef)가 호출될 때, mockList를 반환하도록 설정합니다.
        TypeReference<List<CurriculumDetailResponse>> typeRef = new TypeReferenceCapture();
        // 메서드가 호출될 때, JSON 문자열을 객체로 변환하는 동작을 모킹하여 mockList를 반환하도록 설정합니다.
        when(objectMapper.readValue(eq(mockJson), argThat(new TypeReferenceCaptureMatcher(typeRef)))).thenReturn(mockList);

        List<CurriculumDetailResponse> result = curriculumRedisService.getLatestCurricula();
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Teacher 1", result.get(0).getTeacherName());

        // Verify that the mocks were called as expected
        verify(redisTemplate).opsForValue();
        verify(valueOperations).get(LATEST_CURRICULUMS_KEY);
    }



    @Test
    @DisplayName("인기 있는 커리큘럼 저장 테스트")
    void testSavePopularRatioCurricula() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        String mockJson = "mockJson";

        // when
        when(objectMapper.writeValueAsString(mockList)).thenReturn(mockJson);

        curriculumRedisService.savePopularRatioCurricula(mockList);

        // then
        verify(valueOperations).set(POPULAR_RATIO_CURRICULUMS_KEY, mockJson);
    }


    @Test
    @DisplayName("최신 커리큘럼 추가 테스트")
    void testAddLatestCurriculum() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mockList.add(createMockCurriculumDetailResponse(i));
        }
        String mockJson = "mockJson";
        CurriculumDetailResponse newCurriculum = createMockCurriculumDetailResponse(7);

        // when
        when(valueOperations.get(LATEST_CURRICULUMS_KEY)).thenReturn(mockJson);
        TypeReference<List<CurriculumDetailResponse>> typeRef = new TypeReference<>() {};
        when(objectMapper.readValue(eq(mockJson), eq(typeRef))).thenReturn(mockList);
        when(objectMapper.writeValueAsString(anyList())).thenReturn(mockJson);      when(objectMapper.writeValueAsString(anyList())).thenReturn(mockJson);

        curriculumRedisService.addLatestCurriculum(newCurriculum);

        // then
        verify(valueOperations).set(eq("latestCurriculums"), anyString());
    }

    @Test
    @DisplayName("최신 커리큘럼 저장 테스트")
    void testSaveLatestCurricula() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        String mockJson = "mockJson";

        // when
        when(objectMapper.writeValueAsString(mockList)).thenReturn(mockJson);

        curriculumRedisService.saveLatestCurricula(mockList);

        // then
        verify(valueOperations).set(LATEST_CURRICULUMS_KEY, mockJson);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
