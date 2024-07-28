package com.twentyone.steachserver.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.Role;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * RestAssured를 사용하는 이유
 * 실제 네트워크 요청: RestAssured는 실제 HTTP 요청을 통해 테스트를 수행하므로, 클라이언트와 서버 간의 통신을 포함한 엔드 투 엔드 테스트를 수행할 수 있습니다. 이는 MockMvc와 같은 도구와 비교했을 때 실제 서비스 환경과 더 유사합니다.
 * 직관적인 DSL: RestAssured는 매우 직관적인 도메인 특화 언어(DSL)를 제공하여 HTTP 요청을 만들고 응답을 검증하는 작업을 쉽게 할 수 있습니다. 코드가 읽기 쉽고 유지보수가 용이합니다.
 * 강력한 검증 기능: 응답 상태 코드, 헤더, 본문 등을 쉽게 검증할 수 있는 다양한 메서드를 제공합니다. 이를 통해 테스트의 정확성과 신뢰성을 높일 수 있습니다.
 * 통합 테스트: 실제 네트워크 레이어를 통해 요청을 보내기 때문에, 데이터베이스와 같은 외부 시스템과의 통합을 포함한 테스트를 수행할 수 있습니다. 이는 애플리케이션의 전체적인 동작을 확인하는 데 유용합니다.
 * 자동화된 테스트: RestAssured를 사용하면 자동화된 테스트 스크립트를 작성하여 지속적인 통합 및 배포(CI/CD) 파이프라인에 쉽게 통합할 수 있습니다.
 */

/**
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS) 역할: JUnit 5에서 테스트 클래스의 인스턴스 생명주기를 제어합니다.
 * 설명: Lifecycle.PER_CLASS를 사용하면 테스트 클래스의 인스턴스가 클래스당 하나만 생성됩니다.
 * 기본적으로 JUnit 5는 각 테스트 메서드마다 새로운 테스트 클래스 인스턴스를 생성하지만, PER_CLASS를 사용하면 테스트 클래스당 하나의 인스턴스만 생성되므로 테스트 메서드 간의 상태를 공유할 수 있습니다.
 */
// Jacoco: Java 코드의 커버리지를 체크하는 라이브러리
// 테스트가 한글로 되어 있어 가독성이 전체적으로 크게 향상된 것이 이 단점을 덮고도 남을 장점이 아닌가 한다.

// Spring에서 테스트 메서드에 @Transactional 어노테이션을 사용하면, 기본적으로 테스트가 종료된 후 트랜잭션이 자동으로 롤백됩니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("강사의 수업 인수 테스트")
public class TeacherLoginToLectureAcceptanceTest extends AcceptanceTest {

    /**
     * ParameterizeTest Permalink
     * 여러 가지 인자 값들을 테스트하고 싶은데 여러 개의 메서드들을 생성하기는 비용이 너무 커진다. 하나의 메서드에서 모든 인자 값들을 테스트해 볼 수 없을까?
     *
     * @ParameterizedTest
     * @MethodSource("별점과_평균_리스트_만들기") void 식품의_평균_별점을_계산할_수_있다(List<Integer> 별점_리스트, double 예상_결과) {
     * // given
     * Reviews 리뷰리스트 = 별점으로_리뷰만들기(별점_리스트);
     * <p>
     * // when
     * double 계산_결과 = 리뷰리스트.calculateRatingAverage();
     * <p>
     * // then
     * assertThat(계산_결과).isEqualTo(예상_결과);
     * }
     * <p>
     * private static Stream<Arguments> 별점과_평균_리스트_만들기() {
     * return Stream.of(
     * Arguments.of(emptyList(), 0),
     * Arguments.of(List.of(1, 2, 3, 4, 5), 3.0),
     * Arguments.of(List.of(2, 3, 3, 5, 5), 3.6),
     * Arguments.of(List.of(1, 2, 4), 2.3),
     * Arguments.of(List.of(4, 4, 4, 5, 5), 4.4),
     * Arguments.of(List.of(1, 2, 5), 2.7)
     * );
     * }
     * <p>
     * 위와 같이 해당 method를 이용한 @MethodSource로도 가능하고 @ValueSource, @CsvSource 등 여러 가지 ParameterizeTest를 적극적으로 이용해 보자.
     */

    @Autowired
    private ObjectMapper objectMapper;

    private String teacherAuthToken;
    private Integer curriculumId;

    String 강사_로그인_아이디;
    Map<String, String> 강사_로그인_정보;
    Map<String, String> 강사_추가_정보;
    String 강사_토큰_정보;

    // 테스트 대상 메서드에 @Test를 붙이면 해당 메서드가 속한 클래스는 ‘테스트 클래스’로서 동작하며, 각 메서드는 독립적인 테스트가 된다.
    // 하지만, 테스트마다 테스트 컨텍스트를 매번 새로 생성하게 된다면 오버헤드가 크기 때문에 전체 테스트의 실행 속도가 느려져셔 개발자의 생산성이 떨어진다.
    // 이때문에 테스트 컨텍스트는 자신이 담당하는 테스트 인스턴스에 대한 컨텍스트 관리 및 캐싱 기능을 제공한다!
    @Test
    @Order(1)
    @DisplayName("아이디 중복 검사")
    void testCheckDuplicateUsername(){
        // given
        강사_로그인_아이디 = "t" + UUID.randomUUID().toString().substring(0, 8);
        // when
        Response 아이디_중복_확인 = 아이디_중복_확인(강사_로그인_아이디);
        // then
        아이디_중복_확인_응답_검증(아이디_중복_확인, true);
    }


    Response 아이디_중복_확인(String 강사_로그인_아이디) {
        return
                RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/check-username/" + 강사_로그인_아이디)
                .then().log().all()
                .extract().response();
    }

    void 아이디_중복_확인_응답_검증(Response 아이디_중복_확인, boolean expectedAvailability) {
        아이디_중복_확인.then().statusCode(HttpStatus.OK.value())
                .body("can_use", equalTo(expectedAvailability));
    }

    @Test
    @Order(2)
    @DisplayName("강사 회원가입")
    void testTeacherSignup() throws Exception {
        // given
        강사_로그인_정보 = Map.of(
                "username", 강사_로그인_아이디,
                "password", "teacherPassword");

        강사_추가_정보 = Map.of(
                "name", "teacherSihyun",
                "email", "sihyun" + UUID.randomUUID().toString().substring(0, 4) + "@gmail.com");

//        File resume = new File("src/test/resources/resume.png");
        // when
        Response 회원가입 = 회원가입(강사_로그인_정보, 강사_추가_정보);
//        Response 회원가입 = 회원가입(강사_로그인_정보, 강사_추가_정보, resume);

        // Then
        teacherAuthToken = 회원가입_정보_확인(회원가입, 강사_추가_정보, "TEACHER");

    }

    Response 회원가입(Map<String, String> 강사_로그인_정보, Map<String, String> 강사_추가_정보) throws JsonProcessingException {
        TeacherSignUpDto teacherSignUpDto = TeacherSignUpDto.builder()
                .username(강사_로그인_정보.get("username"))
                .password(강사_로그인_정보.get("password"))
                .name(강사_추가_정보.get("name"))
                .email(강사_추가_정보.get("email"))
                .build();

        return given()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                .multiPart("image", resume, "application/multipart/form-data")
                .multiPart("image", "resume.png", "dummy content".getBytes(), MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .multiPart("teacherSignUpDto", objectMapper.writeValueAsString(teacherSignUpDto), MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/api/v1/teacher/join");
    }

    String 회원가입_정보_확인(Response 회원가입, Map<String, String> 강사_추가_정보, String role) {
        회원가입
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("role", equalTo(role))
                .body("email", equalTo(강사_추가_정보.get("email")))
                .body("name", equalTo(강사_추가_정보.get("name")));

        String token = 회원가입.jsonPath().getString("token");

        if (token.isEmpty()) {
            throw new RuntimeException("JWT token not found in the login response");
        }
        return token;
    }

    @Test
    @Order(3)
    @DisplayName("강사 로그인")
    void testTeacherSignupAndLogin() throws Exception {
        // when
        // given
        Response 로그인 = 로그인(강사_로그인_정보);
        // then
        강사_토큰_정보 = 로그인_정보_확인(로그인, 강사_추가_정보, "TEACHER");
    }

    Response 로그인(Map<String, String> 강사_로그인_정보) throws JsonProcessingException {
        LoginDto loginDto = LoginDto.builder()
                .username(강사_로그인_정보.get("username"))
                .password(강사_로그인_정보.get("password"))
                .build();

        return RestAssured
                .given().log().all()
                .body(objectMapper.writeValueAsString(loginDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/api/v1/login");
    }

    String 로그인_정보_확인(Response 로그인, Map<String, String> 강사_추가_정보, String role) {
        로그인
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("role", equalTo(role))
                .body("email", equalTo(강사_추가_정보.get("email")))
                .body("name", equalTo(강사_추가_정보.get("name")));

        String token = 로그인.jsonPath().getString("token");

        if (token.isEmpty()) {
            throw new RuntimeException("JWT token not found in the login response");
        }
        return token;
    }




    @Test
    @Order(4)
    @DisplayName("강사가 커리큘럼 생성")
    void testCreateCurriculum() throws Exception {
        curriculumId = 커리큘럼_생성(teacherAuthToken, "새로운 커리큘럼", "부제목", "소개", "정보", CurriculumCategory.EDUCATION, "하위 카테고리", "http://example.com/banner.jpg", LocalDate.now(), LocalDate.now().plusDays(10), "1111100", LocalTime.of(10, 0), LocalTime.of(12, 0), 4);
    }

    @Test
    @Order(5)
    @DisplayName("커리큘럼 조회")
    void testGetCurriculum() throws Exception {
        커리큘럼_조회(teacherAuthToken, curriculumId, "새로운 커리큘럼", "부제목", "소개", "정보", CurriculumCategory.EDUCATION.toString(), "하위 카테고리", "http://example.com/banner.jpg", LocalDate.now(), LocalDate.now().plusDays(10), "1111100", LocalTime.of(10, 0), LocalTime.of(12, 0), 4);
    }

    @Test
    @Order(6)
    @DisplayName("강사가 강의하는 커리큘럼 목록 조회")
    void testGetTeacherCurricula() throws Exception {
        강사_강의_커리큘럼_목록_조회(teacherAuthToken, 1, 1, 10, "새로운 커리큘럼", "부제목", "소개", "정보", CurriculumCategory.EDUCATION.toString(), "하위 카테고리", "http://example.com/banner.jpg", LocalDate.now(), LocalDate.now().plusDays(10), "1111100", LocalTime.of(10, 0), LocalTime.of(12, 0), 4);
    }

//    @Test
//    @Order(7)
//    @DisplayName("강사 회원 정보 조회")
//    void testGetTeacherInfo() throws Exception {
//        강사_회원정보_조회(teacherAuthToken, 강사_로그인_아이디, 강사_추가_정보);
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("강사 회원 정보 수정")
//    void testUpdateTeacherInfo() throws Exception {
//        강사_회원정보_수정(teacherAuthToken, "updatedName", "updatedEmail@gmail.com", "updatedPathQualification", "updatedBriefIntroduction", "updatedAcademicBackground", "updatedSpecialization");
//    }

//    @Test
//    @Order(9)
//    @DisplayName("강사 회원 정보 수정")
//    void testUpdateTeacherInfo() throws Exception {
//        강사_회원정보_수정(teacherAuthToken, "updatedName", "updatedEmail@gmail.com", "updatedPathQualification", "updatedBriefIntroduction", "updatedAcademicBackground", "updatedSpecialization");
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("강의의 퀴즈 생성")
//    void testCreateQuiz() throws Exception {
//        퀴즈 생성
//        (teacherAuthToken, "updatedName", "updatedEmail@gmail.com", "updatedPathQualification", "updatedBriefIntroduction", "updatedAcademicBackground", "updatedSpecialization")
//        ;
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("강의의 퀴즈 조회")
//    void testCreateQuiz() throws Exception {
//        퀴즈 조회
//        (teacherAuthToken, "updatedName", "updatedEmail@gmail.com", "updatedPathQualification", "updatedBriefIntroduction", "updatedAcademicBackground", "updatedSpecialization")
//        ;
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("강의의 퀴즈 조회")
//    void testCreateQuiz() throws Exception {
//        퀴즈 조회
//        (teacherAuthToken, "updatedName", "updatedEmail@gmail.com", "updatedPathQualification", "updatedBriefIntroduction", "updatedAcademicBackground", "updatedSpecialization")
//        ;
//    }


    private Integer 커리큘럼_생성(String token, String title, String subTitle, String intro, String information, CurriculumCategory category, String subCategory, String bannerImgUrl, LocalDate startDate, LocalDate endDate, String weekdaysBitmask, LocalTime lectureStartTime, LocalTime lectureEndTime, int maxAttendees) throws Exception {
        CurriculumAddRequest curriculumRequest = CurriculumAddRequest.builder()
                .title(title)
                .subTitle(subTitle)
                .intro(intro)
                .information(information)
                .category(category)
                .subCategory(subCategory)
                .bannerImgUrl(bannerImgUrl)
                .startDate(startDate)
                .endDate(endDate)
                .weekdaysBitmask(weekdaysBitmask)
                .lectureStartTime(lectureStartTime)
                .lectureEndTime(lectureEndTime)
                .maxAttendees(maxAttendees)
                .build();

        Response response = 선생님_토큰(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(curriculumRequest))
                .when()
                .post("/api/v1/curricula")
                .then()
                .statusCode(200)
                .body("title", equalTo(title))
                .extract()
                .response();

        CurriculumDetailResponse createdCurriculum = objectMapper.readValue(response.asString(), CurriculumDetailResponse.class);
        return createdCurriculum.getCurriculumId();
    }

    private void 커리큘럼_조회(String token, Integer curriculumId, String title, String subTitle, String intro, String information, String category, String subCategory, String bannerImgUrl, LocalDate startDate, LocalDate endDate, String weekdaysBitmask, LocalTime lectureStartTime, LocalTime lectureEndTime, int maxAttendees) {
        선생님_토큰(token)
                .when()
                .get("/api/v1/curricula/" + curriculumId)
                .then()
                .statusCode(200)
                .body("title", equalTo(title))
                .body("sub_title", equalTo(subTitle))
                .body("intro", equalTo(intro))
                .body("information", equalTo(information))
                .body("category", equalTo(category))
                .body("sub_category", equalTo(subCategory))
                .body("banner_img_url", equalTo(bannerImgUrl))
                .body("start_date", equalTo(startDate.toString()))
                .body("end_date", equalTo(endDate.toString()))
                .body("weekdays_bitmask", equalTo(weekdaysBitmask))
                .body("lecture_start_time", equalTo(lectureStartTime.toString()))
                .body("lecture_end_time", equalTo(lectureEndTime.toString()))
                .body("max_attendees", equalTo(maxAttendees));
    }

    private static RequestSpecification 선생님_토큰(String token) {
        return given()
                .header("Authorization", "Bearer " + token);
    }

    private void 강사_강의_커리큘럼_목록_조회(String token, int expectedCurriculumCount, int currentPageNumber, int pageSize, String title, String subTitle, String intro, String information, String category, String subCategory, String bannerImgUrl, LocalDate startDate, LocalDate endDate, String weekdaysBitmask, LocalTime lectureStartTime, LocalTime lectureEndTime, int maxAttendees) {
        given()
                .header("Authorization", "Bearer " + token)
                .param("pageSize", String.valueOf(pageSize))
                .param("currentPageNumber", String.valueOf(currentPageNumber))
                .when()
                .get("/api/v1/teachers/curricula")
                .then()
                .statusCode(200)
                .body("curricula", hasSize(expectedCurriculumCount))
                .body("curricula[0].title", equalTo(title))
                .body("curricula[0].sub_title", equalTo(subTitle))
                .body("curricula[0].intro", equalTo(intro))
                .body("curricula[0].information", equalTo(information))
                .body("curricula[0].category", equalTo(category))
                .body("curricula[0].sub_category", equalTo(subCategory))
                .body("curricula[0].banner_img_url", equalTo(bannerImgUrl))
                .body("curricula[0].start_date", equalTo(startDate.toString()))
                .body("curricula[0].end_date", equalTo(endDate.toString()))
                .body("curricula[0].weekdays_bitmask", equalTo(weekdaysBitmask))
                .body("curricula[0].lecture_start_time", equalTo(lectureStartTime.toString()))
                .body("curricula[0].lecture_end_time", equalTo(lectureEndTime.toString()))
                .body("curricula[0].max_attendees", equalTo(maxAttendees))
                .body("current_page_number", equalTo(currentPageNumber))
                .body("total_page", equalTo(1))
                .body("page_size", equalTo(pageSize));
    }

    private void 강사_회원정보_조회(String token, String username, String name, String email) {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/teachers")
                .then()
                .statusCode(200)
                .body("username", equalTo(username))
                .body("name", equalTo(name))
                .body("email", equalTo(email));
    }

    private void 강사_회원정보_수정(String token, String name, String email, String pathQualification, String briefIntroduction, String academicBackground, String specialization) throws Exception {
        TeacherInfoRequest updateRequest = TeacherInfoRequest.builder()
                .name(name)
                .email(email)
                .pathQualification(pathQualification)
                .briefIntroduction(briefIntroduction)
                .academicBackground(academicBackground)
                .specialization(specialization)
                .build();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(updateRequest))
                .when()
                .patch("/api/v1/teachers")
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("email", equalTo(email))
                .body("brief_introduction", equalTo(briefIntroduction))
                .body("academic_background", equalTo(academicBackground))
                .body("specialization", equalTo(specialization));
    }
}
