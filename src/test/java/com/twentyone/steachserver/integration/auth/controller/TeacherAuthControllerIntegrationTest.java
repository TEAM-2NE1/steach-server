package com.twentyone.steachserver.integration.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.MemberCheckPasswordRequestDto;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.integration.ControllerIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("강사 인증 컨트롤러 통합 테스트")
public class TeacherAuthControllerIntegrationTest extends ControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    String 강사_로그인_아이디;
    Map<String, String> 강사_로그인_정보;
    Map<String, String> 강사_추가_정보;
    public String 강사_토큰_정보;

    String 강사_권한;

    Map<String, String> 강사_수정_기본_정보;


    @Test
    @Order(1)
    @DisplayName("강사 회원가입")
    void testTeacherSignup() throws Exception {
        강사_로그인_아이디 = "te" + UUID.randomUUID().toString().substring(0, 6);

        // given
        강사_로그인_정보 = Map.of(
                "username", 강사_로그인_아이디,
                "password", "teacherPassword");

        강사_추가_정보 = Map.of(
                "nickname", "teacherSihyun",
                "email", "sihyun" + UUID.randomUUID().toString().substring(0, 4) + "@gmail.com");

        // when
        Response 회원가입 = 회원가입(강사_로그인_정보, 강사_추가_정보);
//        Response 회원가입 = 회원가입(강사_로그인_정보, 강사_추가_정보, resume);

        // Then
        강사_권한 = "TEACHER";
        강사_토큰_정보 = 회원가입_정보_확인(회원가입, 강사_추가_정보, 강사_권한);
    }

    @Test
    @Order(2)
    @DisplayName("강사 로그인")
    void testTeacherSignupAndLogin() throws Exception {
        // given
        // when
        Response 로그인 = 로그인(강사_로그인_정보);
        // then
        강사_토큰_정보 = 로그인_정보_확인(로그인, 강사_추가_정보, 강사_권한);
    }

    @Test
    @Order(3)
    @DisplayName("강사 회원 정보 조회")
    void testGetTeacherInfo() throws Exception {
        // given
        // when
        Response 강사_회원정보_조회 = 강사_회원정보_조회(강사_토큰_정보);
        // then
        강사_회원정보_확인(강사_회원정보_조회, 강사_로그인_아이디, 강사_추가_정보);
    }

    @Test
    @Order(4)
    @DisplayName("강사 회원 정보 수정")
    void testUpdateTeacherInfo() throws Exception {
        String 비밀번호_임시_토큰 = 비밀번호_확인(강사_토큰_정보, 강사_로그인_정보.get("password"));
        // given
        강사_수정_기본_정보 = new HashMap<>();
        강사_수정_기본_정보.put("username", "teach" + UUID.randomUUID().toString().substring(0, 4));
        강사_수정_기본_정보.put("password", "UpdatePassword123@");
        강사_수정_기본_정보.put("nickname", "dummyName");
        강사_수정_기본_정보.put("email", "sisi" + UUID.randomUUID().toString().substring(0, 4) + "@gmail.com");
        강사_수정_기본_정보.put("briefIntroduction", "This is a brief introduction.");
        강사_수정_기본_정보.put("academicBackground", "PhD in Computer Science");
        강사_수정_기본_정보.put("specialization", "Artificial Intelligence");
        // when
        Response 강사_회원정보_수정 = 강사_회원정보_수정(강사_토큰_정보, 강사_수정_기본_정보, 비밀번호_임시_토큰);
        // then
        강사_회원정보_수정_확인(강사_회원정보_수정, 강사_로그인_아이디, 강사_수정_기본_정보);
    }

    String 비밀번호_확인(String 강사_토큰_정보, String 패스워드) throws JsonProcessingException {
        MemberCheckPasswordRequestDto checkPasswordRequestDto = MemberCheckPasswordRequestDto.builder()
                .password(패스워드)
                .build();

        Response 비밀번호_확인 = given().log().all()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .body(objectMapper.writeValueAsString(checkPasswordRequestDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/api/v1/check/password");

        비밀번호_확인
                .then()
                .body("password_auth_token", Matchers.notNullValue());

        return 비밀번호_확인.jsonPath().getString("password_auth_token");
    }

    Response 회원가입(Map<String, String> 강사_로그인_정보, Map<String, String> 강사_추가_정보) throws JsonProcessingException {
        TeacherSignUpDto teacherSignUpDto = TeacherSignUpDto.builder()
                .username(강사_로그인_정보.get("username"))
                .password(강사_로그인_정보.get("password"))
                .nickname(강사_추가_정보.get("nickname"))
                .email(강사_추가_정보.get("email"))
                .build();


        return given().log().all()
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
                .body("nickname", equalTo(강사_추가_정보.get("nickname")));

        String token = 회원가입.jsonPath().getString("token");

        if (token.isEmpty()) {
            throw new RuntimeException("JWT token not found in the login response");
        }
        return token;
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
                .body("nickname", equalTo(강사_추가_정보.get("nickname")));

        String token = 로그인.jsonPath().getString("token");

        if (token.isEmpty()) {
            throw new RuntimeException("JWT token not found in the login response");
        }
        return token;
    }

    Response 강사_회원정보_조회(String 강사_토큰_정보) {
        return given()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .when()
                .get("/api/v1/teachers");
    }

    void 강사_회원정보_확인(Response 강사_회원정보_조회, String 강사_로그인_아이디, Map<String, String> 강사_추가_정보) {
        강사_회원정보_조회
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(강사_로그인_아이디))
                .body("nickname", equalTo(강사_추가_정보.get("nickname")))
                .body("email", equalTo(강사_추가_정보.get("email")));
    }

    Response 강사_회원정보_수정(String 강사_토큰_정보, Map<String, String> 강사_수정_기본_정보, String 비밀번호_임시_토큰) throws Exception {
        TeacherInfoRequest updateRequest = TeacherInfoRequest.builder()
                .nickname(강사_수정_기본_정보.get("nickname"))
                .email(강사_수정_기본_정보.get("email"))
                .password(강사_수정_기본_정보.get("password"))
                .briefIntroduction(강사_수정_기본_정보.get("briefIntroduction"))
                .academicBackground(강사_수정_기본_정보.get("academicBackground"))
                .specialization(강사_수정_기본_정보.get("specialization"))
                .passwordAuthToken(비밀번호_임시_토큰)
                .build();

        return given()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(updateRequest))
                .when()
                .patch("/api/v1/teachers");
    }

    void 강사_회원정보_수정_확인(Response 강사_회원정보_수정, String 강사_로그인_아이디, Map<String, String> 강사_수정_기본_정보) {
        System.out.println(강사_회원정보_수정.getBody().asString());
        강사_회원정보_수정
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(강사_로그인_아이디))
                .body("nickname", equalTo(강사_수정_기본_정보.get("nickname")))
                .body("email", equalTo(강사_수정_기본_정보.get("email")))
                .body("specialization", equalTo(강사_수정_기본_정보.get("specialization")))
                .body("brief_introduction", equalTo(강사_수정_기본_정보.get("briefIntroduction")))
                .body("academic_background", equalTo(강사_수정_기본_정보.get("academicBackground")));
    }
}
