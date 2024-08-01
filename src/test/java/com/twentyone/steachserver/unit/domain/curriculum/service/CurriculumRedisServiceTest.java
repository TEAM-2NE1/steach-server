
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// springboot의 redis config가 필요함.
@SpringBootTest
@DisplayName("커리큘럼 레디스 서비스 단위 테스트")
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
    @DisplayName("인기 있는 커리큘럼 조회 테스트")
    void testGetPopularRatioCurriculum() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        ObjectMapper actualObjectMapper = new ObjectMapper();
        String mockJson = actualObjectMapper.writeValueAsString(mockList);

        // when
        when(valueOperations.get(POPULAR_RATIO_CURRICULUMS_KEY)).thenReturn(mockJson);
        /*
         * 이 코드는 objectMapper.readValue(mockJson, new TypeReference<List<CurriculumDetailResponse>>() {})가 호출되면 mockList를 반환하도록 모의(Mock)하는 설정입니다.
         * 이는 ObjectMapper가 특정 JSON 문자열(mockJson)을 읽고
         * 특정 타입(TypeReference<List<CurriculumDetailResponse>>)으로 변환할 때 해당 리스트(mockList)를 반환하도록 보장합니다.
         *
         * any(TypeReference.class):
         * Mockito의 any 메서드는 메서드 호출 시 지정된 클래스의 인스턴스와 일치하는 모든 인수를 지정하는 데 사용됩니다.
         * 여기서는 TypeReference.class 타입의 두 번째 인수를 지정합니다.
         */
        TypeReference<List<CurriculumDetailResponse>> typeRef = new TypeReference<>() {};
        when(objectMapper.readValue(eq(mockJson), eq(typeRef))).thenReturn(mockList);

        List<CurriculumDetailResponse> result = curriculumRedisService.getPopularRatioCurriculum();

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

        String mockJson = "mockJson";

        // when
        when(objectMapper.writeValueAsString(mockList)).thenReturn(mockJson);

        curriculumRedisService.savePopularRatioCurricula(mockList);

        // then
        verify(valueOperations).set(POPULAR_RATIO_CURRICULUMS_KEY, mockJson);
    }

    @Test
    @DisplayName("최신 커리큘럼 조회 테스트")
    void testGetLatestCurricula() throws JsonProcessingException {
        // given
        List<CurriculumDetailResponse> mockList = new ArrayList<>();
        mockList.add(createMockCurriculumDetailResponse(1));

        ObjectMapper actualObjectMapper = new ObjectMapper();
        String mockJson = actualObjectMapper.writeValueAsString(mockList);

        // when
        when(valueOperations.get(LATEST_CURRICULUMS_KEY)).thenReturn(mockJson);
        TypeReference<List<CurriculumDetailResponse>> typeRef = new TypeReference<>() {};
        when(objectMapper.readValue(eq(mockJson), eq(typeRef))).thenReturn(mockList);

        List<CurriculumDetailResponse> result = curriculumRedisService.getLatestCurricula();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Teacher 1", result.get(0).getTeacherName());
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
        when(objectMapper.readValue(eq(mockJson), eq(typeRef))).thenReturn(mockList);        when(objectMapper.writeValueAsString(anyList())).thenReturn(mockJson);

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
