package com.twentyone.steachserver.integration.curriculum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.auth.WithAuthTeacher;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.integration.ControllerIntegrationTest;
import com.twentyone.steachserver.integration.auth.controller.TeacherAuthControllerIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("강사의 커리큘럼 통합 테스트")
public class CurriculumControllerIntegrationTest extends TeacherAuthControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    Map<String, Object> 커리큘럼_기본_정보;
    Integer 커리큘럼_PK;
    Map<String, Integer> 커리큘럼_페이징_정보;

    @Test
    @Order(5)
    @DisplayName("강사가 커리큘럼 생성")
    @WithAuthTeacher
    void testCreateCurriculum() throws Exception {
        // given
        커리큘럼_기본_정보 = new HashMap<>();
        커리큘럼_기본_정보.put("title", "새로운 커리큘럼");
        커리큘럼_기본_정보.put("subTitle", "부제목");
        커리큘럼_기본_정보.put("intro", "소개");
        커리큘럼_기본_정보.put("information", "정보");
        커리큘럼_기본_정보.put("category", CurriculumCategory.EDUCATION);
        커리큘럼_기본_정보.put("subCategory", "하위 카테고리");
        커리큘럼_기본_정보.put("bannerImgUrl", "http://example.com/banner.jpg");
        커리큘럼_기본_정보.put("startDate", LocalDate.now());
        커리큘럼_기본_정보.put("endDate", LocalDate.now().plusDays(10));
        커리큘럼_기본_정보.put("weekdaysBitmask", "1111100");
        커리큘럼_기본_정보.put("lectureStartTime", LocalTime.of(10, 0));
        커리큘럼_기본_정보.put("lectureEndTime", LocalTime.of(12, 0));
        커리큘럼_기본_정보.put("maxAttendees", 4);
        // when
        Response 커리큘럼_생성 = 커리큘럼_생성(강사_토큰_정보, 커리큘럼_기본_정보);
        // then
        커리큘럼_PK = 커리큘럼_생성_확인(커리큘럼_생성, 커리큘럼_기본_정보);
    }

    @Test
    @Order(6)
    @DisplayName("커리큘럼 조회")
    void testGetCurriculum() {
        // given
        // when
        Response 커리큘럼_조회 = 커리큘럼_조회();
        // then
        커리큘럼_조회_확인(커리큘럼_조회, 커리큘럼_기본_정보);
    }

    @Test
    @Order(6)
    @DisplayName("강사가 강의하는 커리큘럼 목록 조회")
    @WithAuthTeacher
    void testGetTeacherCurricula() {
//     given
        커리큘럼_페이징_정보 = new HashMap<>();
        커리큘럼_페이징_정보.put("currentPageNumber", 1);
        커리큘럼_페이징_정보.put("pageSize", 2);
//     when
        Response 강사_커리큘럼_조회 = 강사_커리큘럼_조회(강사_토큰_정보, 커리큘럼_페이징_정보);
//     then
        강사_커리큘럼_조회_확인(강사_커리큘럼_조회, 커리큘럼_기본_정보, 커리큘럼_페이징_정보);
    }

    Response 커리큘럼_생성(String 강사_토큰_정보, Map<String, Object> 커리큘럼_기본_정보) throws Exception {
        CurriculumAddRequest curriculumRequest = CurriculumAddRequest.builder()
                .title((String) 커리큘럼_기본_정보.get("title"))
                .subTitle((String) 커리큘럼_기본_정보.get("subTitle"))
                .intro((String) 커리큘럼_기본_정보.get("intro"))
                .information((String) 커리큘럼_기본_정보.get("information"))
                .category((CurriculumCategory) 커리큘럼_기본_정보.get("category"))
                .subCategory((String) 커리큘럼_기본_정보.get("subCategory"))
                .bannerImgUrl((String) 커리큘럼_기본_정보.get("bannerImgUrl"))
                .startDate((LocalDate) 커리큘럼_기본_정보.get("startDate"))
                .endDate((LocalDate) 커리큘럼_기본_정보.get("endDate"))
                .weekdaysBitmask((String) 커리큘럼_기본_정보.get("weekdaysBitmask"))
                .lectureStartTime((LocalTime) 커리큘럼_기본_정보.get("lectureStartTime"))
                .lectureEndTime((LocalTime) 커리큘럼_기본_정보.get("lectureEndTime"))
                .maxAttendees((Integer) 커리큘럼_기본_정보.get("maxAttendees"))
                .build();

        return given()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(curriculumRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/curricula");

    }

    Integer 커리큘럼_생성_확인(Response 커리큘럼_생성, Map<String, Object> 커리큘럼_기본_정보) throws JsonProcessingException {
        String s = 커리큘럼_생성.body().prettyPrint();
        System.out.println(s);
        커리큘럼_생성
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(커리큘럼_기본_정보.get("title"))).body("title", equalTo(커리큘럼_기본_정보.get("title")))
                .body("sub_title", equalTo(커리큘럼_기본_정보.get("subTitle"))).body("title", equalTo(커리큘럼_기본_정보.get("title")))
                .body("intro", equalTo(커리큘럼_기본_정보.get("intro")))
                .body("information", equalTo(커리큘럼_기본_정보.get("information")))
                .body("category", equalTo(커리큘럼_기본_정보.get("category").toString()))
                .body("banner_img_url", equalTo(커리큘럼_기본_정보.get("bannerImgUrl")))
                .body("start_date", equalTo(커리큘럼_기본_정보.get("startDate").toString()))
                .body("end_date", equalTo(커리큘럼_기본_정보.get("endDate").toString()))
                .body("weekdays_bitmask", equalTo(커리큘럼_기본_정보.get("weekdaysBitmask")))
                .body("lecture_start_time", equalTo(커리큘럼_기본_정보.get("lectureStartTime").toString()))
                .body("lecture_end_time", equalTo(커리큘럼_기본_정보.get("lectureEndTime").toString()))
                .body("max_attendees", equalTo(커리큘럼_기본_정보.get("maxAttendees")));

        CurriculumDetailResponse createdCurriculum = objectMapper.readValue(커리큘럼_생성.asString(), CurriculumDetailResponse.class);
        return createdCurriculum.getCurriculumId();
    }

    Response 커리큘럼_조회() {
        return given()
                .when()
                .get("/api/v1/curricula/" + 커리큘럼_PK);
    }

    void 커리큘럼_조회_확인(Response 커리큘럼_조회, Map<String, Object> 커리큘럼_기본_정보) {
        커리큘럼_조회
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(커리큘럼_기본_정보.get("title")))
                .body("intro", equalTo(커리큘럼_기본_정보.get("intro")))
                .body("information", equalTo(커리큘럼_기본_정보.get("information")))
                .body("category", equalTo(커리큘럼_기본_정보.get("category").toString()))
                .body("banner_img_url", equalTo(커리큘럼_기본_정보.get("bannerImgUrl")))
                .body("start_date", equalTo(커리큘럼_기본_정보.get("startDate").toString()))
                .body("end_date", equalTo(커리큘럼_기본_정보.get("endDate").toString()))
                .body("weekdays_bitmask", equalTo(커리큘럼_기본_정보.get("weekdaysBitmask")))
                .body("lecture_start_time", equalTo(커리큘럼_기본_정보.get("lectureStartTime").toString()))
                .body("lecture_end_time", equalTo(커리큘럼_기본_정보.get("lectureEndTime").toString()))
                .body("max_attendees", equalTo(커리큘럼_기본_정보.get("maxAttendees")));
    }

    Response 강사_커리큘럼_조회(String 강사_토큰_정보, Map<String, Integer> 페이징_정보) {
        return given()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .param("pageSize", String.valueOf(페이징_정보.get("pageSize")))
                .param("currentPageNumber", String.valueOf(페이징_정보.get("currentPageNumber")))
                .when()
                .get("/api/v1/teachers/curricula");
    }

    void 강사_커리큘럼_조회_확인(Response 강사_커리큘럼_조회, Map<String, Object> 커리큘럼_기본_정보, Map<String, Integer> 커리큘럼_페이징_정보) {
        int pageSize = 커리큘럼_페이징_정보.get("pageSize");
        int currentPageNumber = 커리큘럼_페이징_정보.get("currentPageNumber");
        int 나타내는_총개수 = pageSize * currentPageNumber;

        강사_커리큘럼_조회
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("curricula", hasSize(1))
                .body("curricula", hasSize(lessThan(나타내는_총개수)))
                .body("curricula[0].title", equalTo(커리큘럼_기본_정보.get("title")))
                .body("curricula[0].sub_title", equalTo(커리큘럼_기본_정보.get("subTitle")))
                .body("curricula[0].intro", equalTo(커리큘럼_기본_정보.get("intro")))
                .body("curricula[0].information", equalTo(커리큘럼_기본_정보.get("information")))
                .body("curricula[0].category", equalTo(커리큘럼_기본_정보.get("category").toString()))
                .body("curricula[0].sub_category", equalTo(커리큘럼_기본_정보.get("subCategory")))
                .body("curricula[0].banner_img_url", equalTo(커리큘럼_기본_정보.get("bannerImgUrl")))
                .body("curricula[0].start_date", equalTo(커리큘럼_기본_정보.get("startDate").toString()))
                .body("curricula[0].end_date", equalTo(커리큘럼_기본_정보.get("endDate").toString()))
                .body("curricula[0].weekdays_bitmask", equalTo(커리큘럼_기본_정보.get("weekdaysBitmask")))
                .body("curricula[0].lecture_start_time", equalTo(커리큘럼_기본_정보.get("lectureStartTime").toString()))
                .body("curricula[0].lecture_end_time", equalTo(커리큘럼_기본_정보.get("lectureEndTime").toString()))
                .body("curricula[0].max_attendees", equalTo(커리큘럼_기본_정보.get("maxAttendees")));
    }
}
