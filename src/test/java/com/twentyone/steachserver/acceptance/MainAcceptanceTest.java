package com.twentyone.steachserver.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.auth.dto.*;
import com.twentyone.steachserver.domain.classroom.dto.FocusTimeRequestDto;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.lecture.dto.LectureResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.AllLecturesInCurriculaResponseDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizRequestDto;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static com.twentyone.steachserver.helper.CastObject.castList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * RestAssured를 사용하는 이유
 * 실제 네트워크 요청: RestAssured는 실제 HTTP 요청을 통해 테스트를 수행하므로, 클라이언트와 서버 간의 통신을 포함한 엔드 투 엔드 테스트를 수행할 수 있습니다. 이는 MockMvc와 같은 도구와 비교했을 때 실제 서비스 환경과 더 유사합니다.
 * 직관적인 DSL: RestAssured는 매우 직관적인 도메인 특화 언어(DSL)를 제공하여 HTTP 요청을 만들고 응답을 검증하는 작업을 쉽게 할 수 있습니다. 코드가 읽기 쉽고 유지보수가 용이합니다.
 * 강력한 검증 기능: 응답 상태 코드, 헤더, 본문 등을 쉽게 검증할 수 있는 다양한 메서드를 제공합니다. 이를 통해 테스트의 정확성과 신뢰성을 높일 수 있습니다.
 * 통합 테스트: 실제 네트워크 레이어를 통해 요청을 보내기 때문에, 데이터베이스와 같은 외부 시스템과의 통합을 포함한 테스트를 수행할 수 있습니다. 이는 애플리케이션의 전체적인 동작을 확인하는 데 유용합니다.
 * 자동화된 테스트: RestAssured를 사용하면 자동화된 테스트 스크립트를 작성하여 지속적인 통합 및 배포(CI/CD) 파이프라인에 쉽게 통합할 수 있습니다.
 */

// Jacoco: Java 코드의 커버리지를 체크하는 라이브러리
// 테스트가 한글로 되어 있어 가독성이 전체적으로 크게 향상된 것이 이 단점을 덮고도 남을 장점이 아닌가 한다.

// 수용 테스트는 시스템이 최종 사용자의 요구사항을 충족하는지를 확인하는 테스트입니다.
// 이는 전체 시스템이 기대한 대로 동작하는지, 사용자의 시나리오를 통해 확인합니다.
// 전체 시스템의 생성 로직만 해서 최대한 성공로직 내가 테스트하고 싶은 메인 로직만 구현해야한다.

// 수정이나 조회 같은게 아닌 생성만 쭈우우우욱 하고 맨 마지막에 진짜 확인하고 싶었던 최종 그거만 확인하는 느낌(사실 진짜 확인하고 싶었던 이런것도 안해도됨)
@DisplayName("프로젝트 메인 기능 인수 테스트")
public class MainAcceptanceTest extends AcceptanceTest {

// 사용해봐야하는디
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

    String 강사_로그인_아이디;
    Map<String, String> 강사_로그인_정보;
    Map<String, String> 강사_추가_정보;
    String 강사_토큰_정보;
    String 강사_권한;

    Map<String, Object> 커리큘럼_기본_정보;
    Integer 커리큘럼_PK;
    Integer 첫번째_강의_PK;
    Map<String, Object> 퀴즈_정보;

    Integer 생성_개수 = 4;
    List<String> 인증_코드들;

    String 학생_로그인_아이디;
    Map<String, String> 학생_로그인_정보;
    Map<String, String> 학생_추가_정보;
    List<String> 학생_토큰들 = new ArrayList<>();
    // 단순한 파라미터화된 테스트에는 static Stream<학생_회원가입_정보> 학생_회원가입_정보들() 방식을, 복잡한 초기화가 필요한 경우에는 @BeforeAll을 사용하는 것이 좋습니다.

    // 추가: 학생 회원가입 정보 제공 메서드
    static Stream<Arguments> 학생_회원가입_정보들() {
        return Stream.of(
                Arguments.of("testS1", "applepass", "apple", "testS1@example.com", 0),
                Arguments.of("testS2", "bananapass", "banana", "testS2@example.com", 1),
                Arguments.of("testS3", "chickenpass", "chicken", "testS3@example.com", 2),
                Arguments.of("testS4", "pearpass", "pear", "testS4@example.com", 3)
        );
    }

    Stream<Arguments> 학생_토큰들() {
        return this.학생_토큰들.stream().map(Arguments::of);
    }

    // 테스트 대상 메서드에 @Test를 붙이면 해당 메서드가 속한 클래스는 ‘테스트 클래스’로서 동작하며, 각 메서드는 독립적인 테스트가 된다.
    // 하지만, 테스트마다 테스트 컨텍스트를 매번 새로 생성하게 된다면 오버헤드가 크기 때문에 전체 테스트의 실행 속도가 느려져셔 개발자의 생산성이 떨어진다.
    // 이때문에 테스트 컨텍스트는 자신이 담당하는 테스트 인스턴스에 대한 컨텍스트 관리 및 캐싱 기능을 제공한다!
    @Test
    @Order(1)
    @DisplayName("강사 회원가입")
    void testTeacherSignup() throws Exception {
        강사_로그인_아이디 = "test" + UUID.randomUUID().toString().substring(0, 6);

        // given
        강사_로그인_정보 = Map.of(
                "username", 강사_로그인_아이디,
                "password", "teacherPassword");

        강사_추가_정보 = Map.of(
                "nickname", "teacherSihyun",
                "email", "sihyun" + UUID.randomUUID().toString().substring(0, 4) + "@gmail.com");

        // when
        Response 강사_회원가입 = 강사_회원가입(강사_로그인_정보, 강사_추가_정보);
//        Response 회원가입 = 회원가입(강사_로그인_정보, 강사_추가_정보, resume);

        // Then
        강사_권한 = "TEACHER";
        강사_토큰_정보 = 강사_회원가입_정보_확인(강사_회원가입, 강사_추가_정보, 강사_권한);
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
    @DisplayName("강사가 커리큘럼 생성")
    void testCreateCurriculum() throws Exception {
        // given
        커리큘럼_기본_정보 = new HashMap<>();
        커리큘럼_기본_정보.put("title", "새로운 커리큘럼");
        커리큘럼_기본_정보.put("subTitle", "부제목");
        커리큘럼_기본_정보.put("intro", "소개");
        커리큘럼_기본_정보.put("information", "정보");
        커리큘럼_기본_정보.put("category", CurriculumCategory.getCategoryByIndex(0));
        커리큘럼_기본_정보.put("subCategory", "하위 카테고리");
        커리큘럼_기본_정보.put("bannerImgUrl", "https://example.com/banner.jpg");
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
    @Order(4)
    @DisplayName("커리큘럼에_해당하는_강의_조회")
    void testGetLecturesByCurriculum() throws Exception {
        // given
        // when
        Response 커리큘럼에_해당하는_강의_조회 = 커리큘럼의_강의_조회(커리큘럼_PK);
        // then
        첫번째_강의_PK = 커리큘럼의_강의_확인(커리큘럼에_해당하는_강의_조회);
    }

    @Test
    @Order(5)
    @Disabled //TODO 퀴즈 리스트로 만드는 변경사항 따라 수정하기!
    @DisplayName("강사가 첫번째 강의의 퀴즈 생성")
    void testCreateQuiz() throws JsonProcessingException {
        // given
        List<String> 퀴즈_보기_정보 = new ArrayList<>();
        퀴즈_보기_정보.add("가");
        퀴즈_보기_정보.add("나");
        퀴즈_보기_정보.add("다");
        퀴즈_보기_정보.add("라");

//        List<String> 퀴즈_정답_정보 = new ArrayList<>();
//        퀴즈_정답_정보.add("가");
        Integer 퀴즈_정답_정보 = 1; //"가"

        퀴즈_정보 = new HashMap<>();
        퀴즈_정보.put("quizNumber", 1);
        퀴즈_정보.put("question", "가,나,다,라 중 가장 먼저 나오는 것은?");
        퀴즈_정보.put("choices", 퀴즈_보기_정보);
        퀴즈_정보.put("answers", 퀴즈_정답_정보);

        // when
        Response 퀴즈_생성 = 퀴즈_생성(첫번째_강의_PK, 퀴즈_정보);
        // then
        퀴즈_정보_확인(퀴즈_생성, 퀴즈_정보);
    }

    @Test
    @Order(1)
    @DisplayName("인증 코드 생성")
    void testCreateCode() throws JsonProcessingException {
        // given
        // when
        Response 인증_코드_생성 = 인증_코드_생성(생성_개수, 강사_토큰_정보);
        // then
        인증_코드들 = 인증_코드_생성_확인(인증_코드_생성, 생성_개수);
    }

    @Order(2)
    @DisplayName("학생 회원 가입")
    @ParameterizedTest
    @MethodSource("학생_회원가입_정보들")
    void testStudentSignUp(String username,
                           String password,
                           String nickname,
                           String email,
                           Integer number) throws JsonProcessingException {
        // given
        학생_로그인_아이디 = "s" + UUID.randomUUID().toString().substring(0, 7);

        학생_로그인_정보 = new HashMap<>();
        학생_로그인_정보.put("username", username);
        학생_로그인_정보.put("password", password);

        학생_추가_정보 = new HashMap<>();
        학생_추가_정보.put("nickname", nickname);
        학생_추가_정보.put("email", email);
        // when
        Response 학생_회원가입 = 학생_회원가입(학생_로그인_정보, 학생_추가_정보, 인증_코드들.get(number));
        // then
        String 학생_토큰 = 학생_회원가입_정보_확인(학생_회원가입, 학생_로그인_정보, 학생_추가_정보);
        // 추가 작업
        학생_토큰들.add(학생_토큰);
    }

    @Test
    @Order(6)
    @DisplayName("강사 수업 시작")
    void testStartLecture() throws JsonProcessingException {
        // given
        // when
        Response 수업_시작 = 수업_시작(첫번째_강의_PK);
        // then
        수업_시작_확인(수업_시작);
    }


    @Order(6)
    @DisplayName("학생의 커리큘럼 수강 신청")
    @ParameterizedTest
    @MethodSource("학생_토큰들")
    void testStudentEnroll(String 학생_토큰) throws JsonProcessingException {
        // given
        // when
        Response 커리큘럼_수강_신청 = 커리큘럼_수강_신청(커리큘럼_PK, 학생_토큰);
        // then
        커리큘럼_수강_신청_확인(커리큘럼_수강_신청);
    }

    @Order(7)
    @DisplayName("학생의 퀴즈 정답 전달")
    @ParameterizedTest
    @MethodSource("학생_토큰들")
    void testStudentQuiz(String 학생_토큰) throws JsonProcessingException {
        // given
        int 점수 = (int) (Math.random() * 2) * 50;
        String 고른_정답;
        if (점수 != 0) 고른_정답 = "가";
        else {
            고른_정답 = castList(퀴즈_정보.get("choices"), String.class).get((int) (Math.random() * 2) + 1);
        }
        // when
        Response 학생_퀴즈_전달 = 학생_퀴즈_전달(첫번째_강의_PK, 점수, 고른_정답, 학생_토큰);

        // then
        학생_퀴즈_전달_확인(학생_퀴즈_전달, 점수, 고른_정답);
    }

    @Order(7)
    @DisplayName("학생의 집중도 전달")
    @ParameterizedTest
    @MethodSource("학생_토큰들")
    void testStudentFocusTime(String 학생_토큰) throws JsonProcessingException {
        // given
        Integer 집중_시간 = (int) (Math.random() * 5);

        // when
        Response 학생_집중도_전달 = 학생_집중도_전달(첫번째_강의_PK, 집중_시간, 학생_토큰);

        // then
        학생_집중도_전달_확인(학생_집중도_전달);
    }

    @Test
    @Order(8)
    @DisplayName("강사의 수업 종료(통계 계산)")
    void testTeacherSignUp() throws JsonProcessingException {
        // given
        // when
        Response 수업_종료 = 수업_종료(첫번째_강의_PK);
        // then
        수업_종료_통계_확인(수업_종료);
    }

    Response 강사_회원가입(Map<String, String> 강사_로그인_정보, Map<String, String> 강사_추가_정보) throws JsonProcessingException {
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

    String 강사_회원가입_정보_확인(Response 강사_회원가입, Map<String, String> 강사_추가_정보, String role) {
        강사_회원가입
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("role", equalTo(role))
                .body("email", equalTo(강사_추가_정보.get("email")))
                .body("nickname", equalTo(강사_추가_정보.get("nickname")));

        String token = 강사_회원가입.jsonPath().getString("token");

        if (token.isEmpty()) {
            throw new RuntimeException("JWT token not found in the login response");
        }
        return token;
    }


    Response 로그인(Map<String, String> 로그인_정보) throws JsonProcessingException {
        LoginDto loginDto = LoginDto.builder()
                .username(로그인_정보.get("username"))
                .password(로그인_정보.get("password"))
                .build();

        return given().log().all()
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

        return given().log().all()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .body(objectMapper.writeValueAsString(curriculumRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/api/v1/curricula");

    }

    Integer 커리큘럼_생성_확인(Response 커리큘럼_생성, Map<String, Object> 커리큘럼_기본_정보) throws JsonProcessingException {
        String s = 커리큘럼_생성.body().prettyPrint();
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

    Response 커리큘럼의_강의_조회(Integer 커리큘럼_pk) {
        return given().log().all()
                .when().log().all()
                .get("/api/v1/curricula/" + 커리큘럼_pk + "/lectures");
    }

    Integer 커리큘럼의_강의_확인(Response 커리큘럼의_강의_조회) throws JsonProcessingException {
        커리큘럼의_강의_조회
                .then()
                .body("lectures", Matchers.notNullValue());

        // 응답을 WeekLectureListResponseDto 객체로 변환
        AllLecturesInCurriculaResponseDto weekLectureListResponse = objectMapper.readValue(커리큘럼의_강의_조회.asString(), AllLecturesInCurriculaResponseDto.class);

        // 첫 번째 주차의 첫 번째 LectureResponseDto의 id 값 가져오기
        Map<Integer, List<LectureResponseDto>> lecturesMap = weekLectureListResponse.getLectures();
        Integer firstWeek = lecturesMap.keySet().stream().findFirst().orElseThrow(() -> new RuntimeException("No lectures found"));
        Integer firstLectureId = lecturesMap.get(firstWeek).get(0).getLectureId();

        return firstLectureId;
    }

    Response 퀴즈_생성(Integer 첫번째_강의_pk, Map<String, Object> 퀴즈_정보) throws JsonProcessingException {
        List<String> choices = castList(퀴즈_정보.get("choices"), String.class);
        Integer answers = (Integer) 퀴즈_정보.get("answers");

        //TODO QuizListReqeustDto를 만들자!
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .quizNumber((Integer) 퀴즈_정보.get("quizNumber"))
                .question(퀴즈_정보.get("question").toString())
                .choices(choices)
                .answers(answers)
                .build();

        return given().log().all()
                .header("Authorization", "Bearer " + 강사_토큰_정보) // 토큰 정보 설정
                .body(objectMapper.writeValueAsString(quizRequestDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/api/v1/quizzes/" + 첫번째_강의_pk);
    }

    void 퀴즈_정보_확인(Response 퀴즈_생성, Map<String, Object> 퀴즈_정보) {
        퀴즈_생성.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("quiz_number", equalTo(퀴즈_정보.get("quizNumber")))
                .body("question", equalTo(퀴즈_정보.get("question")))
                .body("choices", equalTo(퀴즈_정보.get("choices")))
                .body("answers", equalTo(퀴즈_정보.get("answers")));
    }

    Response 인증_코드_생성(Integer 생성_개수, String 강사_토큰_정보) throws JsonProcessingException {
        AuthCodeRequest authCodeRequest = AuthCodeRequest.builder()
                .numberOfCode(생성_개수).build();
        return given().log().all()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .body(objectMapper.writeValueAsString(authCodeRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/api/v1/auth-codes");
    }

    List<String> 인증_코드_생성_확인(Response 인증_코드_생성, Integer 생성_개수) throws JsonProcessingException {
        인증_코드_생성
                .then()
                .statusCode(HttpStatus.CREATED.value());
        AuthCodeResponse authCodeResponse = objectMapper.readValue(인증_코드_생성.asString(), AuthCodeResponse.class);
        // 첫 번째 LectureResponseDto의 id 값 가져오기
        Assertions.assertEquals(authCodeResponse.getAuthCode().size(), 생성_개수);

        return authCodeResponse.getAuthCode();
    }

    Response 수업_시작(Integer 첫번째_강의_pk) {
        return given().log().all()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .when().log().all()
                .patch("api/v1/lectures/start/" + 첫번째_강의_pk);
    }

    void 수업_시작_확인(Response 수업_시작) {
        수업_시작.then()
                .statusCode(HttpStatus.OK.value());
    }

    Response 학생_회원가입(Map<String, String> 학생_로그인_정보, Map<String, String> 학생_추가_정보, String 인증_코드) throws JsonProcessingException {
        StudentSignUpDto studentSignUpDto = StudentSignUpDto.builder()
                .username(학생_로그인_정보.get("username"))
                .password(학생_로그인_정보.get("password"))
                .nickname(학생_추가_정보.get("nickname"))
                .email(학생_추가_정보.get("email"))
                .auth_code(인증_코드)
                .build();

        return given().log().all()
                .when().log().all()
                .body(objectMapper.writeValueAsString(studentSignUpDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/api/v1/student/join");
    }

    String 학생_회원가입_정보_확인(Response 학생_회원가입, Map<String, String> 학생_로그인_정보, Map<String, String> 학생_추가_정보) {
        학생_회원가입.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("username", equalTo(학생_로그인_정보.get("username")))
                .body("nickname", equalTo(학생_추가_정보.get("nickname")))
                .body("email", equalTo(학생_추가_정보.get("email")));

        String token = 학생_회원가입.jsonPath().getString("token");

        if (token.isEmpty()) {
            throw new RuntimeException("JWT token not found in the login response");
        }
        return token;
    }

    Response 커리큘럼_수강_신청(Integer 커리큘럼_pk, String 학생_토큰) {
        return given().log().all()
                .header("Authorization", "Bearer " + 학생_토큰)
                .when().log().all()
                .post("api/v1/curricula/" + 커리큘럼_pk + "/apply");
    }

    void 커리큘럼_수강_신청_확인(Response 커리큘럼_수강_신청) {
        커리큘럼_수강_신청.then()
                .statusCode(HttpStatus.OK.value());
    }

    Response 학생_퀴즈_전달(Integer 첫번째_강의_pk, Integer 점수, String 고른_답, String 학생_토큰) throws JsonProcessingException {
        StudentQuizRequestDto quizRequest = StudentQuizRequestDto.builder()
                .score(점수)
                .studentChoice(고른_답)
                .build();

        return given().log().all()
                .header("Authorization", "Bearer " + 학생_토큰)
                .body(objectMapper.writeValueAsString(quizRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/api/v1/studentsQuizzes/" + 첫번째_강의_pk);
    }

    void 학생_퀴즈_전달_확인(Response 학생_퀴즈_전달, Integer 점수, String 고른_답) {
        학생_퀴즈_전달.then()
                .statusCode(HttpStatus.OK.value())
                .body("score", equalTo(점수))
                .body("student_choice", equalTo(고른_답));
    }

    Response 학생_집중도_전달(Integer 첫번째_강의_pk, Integer 강의_집중도, String 학생_토큰) throws JsonProcessingException {
        FocusTimeRequestDto focusTimeRequest = new FocusTimeRequestDto(강의_집중도);

        return given().log().all()
                .header("Authorization", "Bearer " + 학생_토큰)
                .body(objectMapper.writeValueAsString(focusTimeRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("api/v1/studentsLectures/focus-time/" + 첫번째_강의_pk);
    }

    void 학생_집중도_전달_확인(Response 학생_집중도_전달) {
        학생_집중도_전달.then()
                .statusCode(HttpStatus.OK.value());
    }

    Response 수업_종료(Integer 첫번째_강의_pk) {
        return given().log().all()
                .header("Authorization", "Bearer " + 강사_토큰_정보)
                .when()
                .get("/api/v1/lectures/final/" + 첫번째_강의_pk);
    }

    void 수업_종료_통계_확인(Response 수업_종료) {
        수업_종료.then()
                .statusCode(HttpStatus.OK.value());
    }

}