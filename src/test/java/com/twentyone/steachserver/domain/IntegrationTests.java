package com.twentyone.steachserver.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import com.twentyone.steachserver.domain.auth.service.JwtService;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @Nested 어노테이션은 JUnit 5에서 사용되는 것으로, 중첩된(non-static) 테스트 클래스임을 나타냅니다. 중첩된 테스트 클래스는 JUnit 5의 강력한 기능으로, 테스트 케이스를 더 세분화된 방식으로 조직할 수 있게 합니다. 이를 통해 관련된 테스트 케이스를 함께 그룹화하고, 테스트 구조를 더 읽기 쉽고 유지보수하기 쉽게 만들 수 있습니다.
 * @Nested 어노테이션의 장점
 * 논리적 그룹화: 관련된 테스트 케이스를 함께 그룹화하여, 테스트가 어떤 맥락에서 실행되는지 이해하기 쉽게 합니다.
 * 공유된 설정 및 해제: 중첩된 클래스는 고유한 @BeforeEach, @AfterEach, @BeforeAll, @AfterAll 메서드를 가질 수 있어, 특정 테스트 그룹에 대해 별도의 설정 및 해제 작업을 수행할 수 있습니다.
 * 캡슐화: 중첩된 클래스는 테스트 로직을 캡슐화하여, 테스트 간의 상호 간섭 위험을 줄여줍니다.
 */
@Nested
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor()
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    private final String authCode;
    /**
     * SecurityMockMvcConfigurers.springSecurity() : Spring Security를 Spring MVC 테스트와 통합할 때 필요한 모든 초기 세팅을 수행한다.
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test()
    @DisplayName("선생님 controller, 커리큘럼 생성, 조회")
    void testTeacherSignupTo() throws Exception {
        String teacherUsername = "t" + UUID.randomUUID().toString().substring(0, 8);
        String teacherEmail = "sihyun" + UUID.randomUUID().toString().substring(0, 9) + "@gmail.com";
        String teacherName = "teacherSihyun";
        final String teacherPassword = "password!!";


        String teacherAuthToken;

        // 아이디 중복 확인
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/check-username/" + teacherUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.can_use").value(true));  // 먼저 사용 가능한지 확인합니다.

        // 강사 회원가입
        TeacherSignUpDto teacherSignUpDto = TeacherSignUpDto.builder()
                .username(teacherUsername)
                .password(teacherPassword)
                .name(teacherName) // 필수 필드 설정
                .email(teacherEmail)
                .build();

        MockMultipartFile file = new MockMultipartFile("file", "resume.txt", "text/plain", "dummy content".getBytes());
        MockMultipartFile teacherSignUpDtoJson = new MockMultipartFile("teacherSignUpDto", "", "application/json", objectMapper.writeValueAsBytes(teacherSignUpDto));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/teacher/join")
                        .file(file)
                        .file(teacherSignUpDtoJson))
                .andExpect(status().isCreated());

        // 아이디 중복 확인 (이미 사용 중인 닉네임임을 확인합니다)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/check-username/" + teacherUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.can_use").value(false));


        // 로그인
        LoginDto loginDto = LoginDto.builder()
                .username(teacherUsername)
                .password(teacherPassword)
                .build();

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        // JWT 반환 추출
        String loginResponseContent = loginResult.getResponse().getContentAsString();
        Map<String, String> loginResponseMap = objectMapper.readValue(loginResponseContent, Map.class);
        teacherAuthToken = loginResponseMap.get("token");
        if (teacherAuthToken == null || teacherAuthToken.isEmpty()) {
            throw new RuntimeException("JWT token not found in the login response");
        }


        // 강사 회원정보 조회
        mockMvc.perform(get("/api/v1/teachers")
                        .header("Authorization", "Bearer " + teacherAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(teacherUsername))
                .andExpect(jsonPath("$.name").value(teacherName))
                .andExpect(jsonPath("$.email").value(teacherEmail));


        // 강사 회원정보 수정
        teacherEmail = "sihyun" + UUID.randomUUID().toString().substring(0, 10) + "@gmail.com";
        teacherName = "조시현";

        TeacherInfoRequest updateRequest = TeacherInfoRequest.builder()
                .name(teacherName)
                .email(teacherEmail)
                .pathQualification("updatedPathQualification")
                .briefIntroduction("updatedBriefIntroduction")
                .academicBackground("updatedAcademicBackground")
                .specialization("updatedSpecialization")
                .build();

        mockMvc.perform(patch("/api/v1/teachers")
                        .header("Authorization", "Bearer " + teacherAuthToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(teacherUsername))
                .andExpect(jsonPath("$.name").value(teacherName))
                .andExpect(jsonPath("$.email").value(teacherEmail))
                .andExpect(jsonPath("$.brief_introduction").value("updatedBriefIntroduction"))
                .andExpect(jsonPath("$.academic_background").value("updatedAcademicBackground"))
                .andExpect(jsonPath("$.specialization").value("updatedSpecialization"));


        // 커리큘럼 생성
        CurriculumAddRequest curriculumRequest = CurriculumAddRequest.builder()
                .title("새로운 커리큘럼")
                .subTitle("부제목")
                .intro("소개")
                .information("정보")
                .category(CurriculumCategory.EDUCATION)
                .subCategory("하위 카테고리")
                .bannerImgUrl("http://example.com/banner.jpg")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .weekdaysBitmask("1111100")
                .lectureStartTime(LocalTime.of(10, 0))
                .lectureEndTime(LocalTime.of(12, 0))
                .maxAttendees(4)
                .build();

        ResultActions authorization = mockMvc.perform(post("/api/v1/curricula")
                .header("Authorization", "Bearer " + teacherAuthToken) // JWT 권한 추가
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(curriculumRequest)));

        String responseContent = authorization
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("새로운 커리큘럼"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        /**
         * JSONPath는 JSON 데이터를 탐색하고 쿼리하기 위한 표현식 언어입니다.
         * $.title와 같은 JSONPath 표현식은 JSON 객체 내에서 특정 필드를 선택하는 데 사용됩니다.
         * 여기서 $.title는 루트 객체 아래에 있는 title 필드를 의미합니다.
         * JSONPath는 XML에서 사용되는 XPath와 유사하지만 JSON 데이터를 처리하도록 설계되었습니다.
         */
        // 4. 제작 커리큘럼 조회
        CurriculumDetailResponse createdCurriculum = objectMapper.readValue(responseContent, CurriculumDetailResponse.class);
        Integer curriculumId = createdCurriculum.getCurriculumId();
        mockMvc.perform(get("/api/v1/curricula/" + curriculumId)
                .header("Authorization", "Bearer " + teacherAuthToken) // JWT 토큰 추가
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("새로운 커리큘럼"))
                .andExpect(jsonPath("$.sub_title").value("부제목"))
                .andExpect(jsonPath("$.intro").value("소개"))
                .andExpect(jsonPath("$.information").value("정보"))
                .andExpect(jsonPath("$.category").value(CurriculumCategory.EDUCATION.toString()))
                .andExpect(jsonPath("$.sub_category").value("하위 카테고리"))
                .andExpect(jsonPath("$.banner_img_url").value("http://example.com/banner.jpg"))
                .andExpect(jsonPath("$.start_date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.end_date").value(LocalDate.now().plusDays(10).toString()))
                .andExpect(jsonPath("$.weekdays_bitmask").value("1111100"))
                .andExpect(jsonPath("$.lecture_start_time").value(LocalTime.of(10, 0).toString()))
                .andExpect(jsonPath("$.lecture_end_time").value(LocalTime.of(12, 0).toString()))
                .andExpect(jsonPath("$.max_attendees").value(4));


        // 강사가 강의하는 커리큘럼 목록 조회
        ResultActions myCourses = mockMvc.perform(get("/api/v1/teachers/curricula")
                        .header("Authorization", "Bearer " + teacherAuthToken)
                        .param("pageSize", "10")
                        .param("currentPageNumber", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.curricula").isArray())
                .andExpect(jsonPath("$.curricula.length()").value(1)) // 현재 테스트에서는 1개의 커리큘럼만 생성되었으므로 1로 검증
                .andExpect(jsonPath("$.curricula[0].title").value("새로운 커리큘럼"))
                .andExpect(jsonPath("$.curricula[0].sub_title").value("부제목"))
                .andExpect(jsonPath("$.curricula[0].intro").value("소개"))
                .andExpect(jsonPath("$.curricula[0].information").value("정보"))
                .andExpect(jsonPath("$.curricula[0].category").value(CurriculumCategory.EDUCATION.toString()))
                .andExpect(jsonPath("$.curricula[0].sub_category").value("하위 카테고리"))
                .andExpect(jsonPath("$.curricula[0].banner_img_url").value("http://example.com/banner.jpg"))
                .andExpect(jsonPath("$.curricula[0].start_date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.curricula[0].end_date").value(LocalDate.now().plusDays(10).toString()))
                .andExpect(jsonPath("$.curricula[0].weekdays_bitmask").value("1111100"))
                .andExpect(jsonPath("$.curricula[0].lecture_start_time").value(LocalTime.of(10, 0).toString()))
                .andExpect(jsonPath("$.curricula[0].lecture_end_time").value(LocalTime.of(12, 0).toString()))
                .andExpect(jsonPath("$.curricula[0].max_attendees").value(4))
                .andExpect(jsonPath("$.current_page_number").value(1))
                .andExpect(jsonPath("$.total_page").value(1))
                .andExpect(jsonPath("$.page_size").value(10));
    }


}
