package com.twentyone.steachserver.integration.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import com.twentyone.steachserver.integration.ControllerIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
public class AuthControllerIntegrationTest extends ControllerIntegrationTest {


    @Autowired
    private ObjectMapper objectMapper;

    String 강사_로그인_아이디;
    Map<String, String> 강사_로그인_정보;
    Map<String, String> 강사_추가_정보;
    String 강사_토큰_정보;
    String 강사_권한;

    @Test
    @Order(1)
    @DisplayName("아이디 중복 검사")
    void testCheckDuplicateUsername() {
        // given
        강사_로그인_아이디 = "t" + UUID.randomUUID().toString().substring(0, 8);
        // when
        Response 아이디_중복_확인 = 아이디_중복_확인(강사_로그인_아이디);
        // then
        아이디_중복_확인_응답_검증(아이디_중복_확인, true);
    }

    @Test
    @Order(2)
    @DisplayName("강사 회원가입")
    void testTeacherSignup() throws Exception {
        // given
        강사_로그인_정보 = new HashMap<>();
        강사_로그인_정보.put("username", 강사_로그인_아이디);
        강사_로그인_정보.put("password", "1234");

        강사_추가_정보 = new HashMap<>();
        강사_추가_정보.put("name", "teacherSihyun");
        강사_추가_정보.put("email", "sihyun" + UUID.randomUUID().toString().substring(0, 4) + "@gmail.com");


        // when
        Response 회원가입 = 회원가입(강사_로그인_정보, 강사_추가_정보);
//        Response 회원가입 = 회원가입(강사_로그인_정보, 강사_추가_정보, resume);

        // Then
        강사_권한 = "TEACHER";
        강사_토큰_정보 = 회원가입_정보_확인(회원가입, 강사_추가_정보, 강사_권한);
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
}
