package com.twentyone.steachserver.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 * 역할: JUnit 5에서 테스트 클래스의 인스턴스 생명주기를 제어합니다.
 * 설명: Lifecycle.PER_CLASS를 사용하면 테스트 클래스의 인스턴스가 클래스당 하나만 생성됩니다. 기본적으로 JUnit 5는 각 테스트 메서드마다 새로운 테스트 클래스 인스턴스를 생성하지만, PER_CLASS를 사용하면 테스트 클래스당 하나의 인스턴스만 생성되므로 테스트 메서드 간의 상태를 공유할 수 있습니다.
 * @TestMethodOrder(OrderAnnotation.class)
 * 역할: 테스트 메서드의 실행 순서를 지정합니다.
 * 설명: OrderAnnotation을 사용하면 @Order 어노테이션을 통해 각 테스트 메서드의 실행 순서를 명시적으로 정의할 수 있습니다. 이를 통해 특정 순서대로 테스트를 실행할 수 있습니다.
 * @SpringBootTest
 * 역할: 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
 * 설명: 통합 테스트를 실행하기 위해 전체 스프링 부트 애플리케이션 컨텍스트를 로드합니다. 이를 통해 실제 애플리케이션 환경에서 테스트를 수행할 수 있습니다. 모든 빈이 로드되고, 실제 애플리케이션과 유사한 환경에서 테스트가 실행됩니다.
 * @AutoConfigureMockMvc
 * 역할: MockMvc를 자동으로 구성합니다.
 * 설명: MockMvc를 자동으로 설정하여 웹 애플리케이션의 컨트롤러 테스트를 쉽게 할 수 있게 합니다. 이를 통해 HTTP 요청 및 응답을 시뮬레이션하여 테스트할 수 있습니다.
 * @ActiveProfiles("test")
 * 역할: 테스트 환경에서 사용할 스프링 프로파일을 지정합니다.
 * 설명: test 프로파일을 활성화하여 테스트 실행 시 특정 설정 파일이나 빈 설정을 사용하도록 합니다. 이를 통해 개발, 테스트, 프로덕션 등 환경별로 다른 설정을 쉽게 적용할 수 있습니다.
 * @RequiredArgsConstructor
 * 역할: Lombok 라이브러리의 어노테이션으로, final 필드와 @NonNull 필드를 포함하는 생성자를 자동으로 생성합니다.
 * 설명: 의존성 주입을 위해 final 필드와 @NonNull 필드를 포함한 생성자를 자동으로 생성해 줍니다. 이를 통해 필드 주입 대신 생성자 주입을 사용할 수 있으며, 생성자를 통해 필요한 의존성을 주입받을 수 있습니다.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeacherLoginToCurriculumCreationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    final String teacherUsername = "t" + UUID.randomUUID().toString().substring(0, 8);
    final String teacherName = "teacherSihyun";
    final String teacherPassword = "password!!";
    final String teacherEmail = "sihyun" + UUID.randomUUID().toString().substring(0, 4) + "@gmail.com";


    private String teacherAuthToken;
    private Integer curriculumId;

    @Test
    @Order(1)
    @DisplayName("아이디 중복확인, 강사 회원가입, 로그인")
    void testTeacherSignupAndLogin() throws Exception {
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

        // 아이디 중복 확인 (이미 사용 중인 닉네임임을 확인합니다)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/check-username/" + teacherUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.can_use").value(false));
    }

    @Test
    @Order(2)
    @DisplayName("강사가 커리큘럼 생성")
    void testCreateCurriculum() throws Exception {
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

        // 제작 커리큘럼 조회
        CurriculumDetailResponse createdCurriculum = objectMapper.readValue(responseContent, CurriculumDetailResponse.class);
        curriculumId = createdCurriculum.getCurriculumId();
    }

    @Test
    @Order(3)
    @DisplayName("커리큘럼 조회")
    void testGetCurriculum() throws Exception {
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
    }

    @Test
    @Order(4)
    @DisplayName("강사가 강의하는 커리큘럼 목록 조회")
    void testGetTeacherCurricula() throws Exception {
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

    @Test
    @Order(5)
    @DisplayName("강사 회원 정보 조회")
    void testGetTeacherInfo() throws Exception {
        // 강사 회원정보 조회
        mockMvc.perform(get("/api/v1/teachers")
                        .header("Authorization", "Bearer " + teacherAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(teacherUsername))
                .andExpect(jsonPath("$.name").value(teacherName))
                .andExpect(jsonPath("$.email").value(teacherEmail));
    }

    @Test
    @Order(6)
    @DisplayName("강사 회원 정보 수정")
    void testUpdateTeacherInfo() throws Exception {
        // 강사 회원정보 수정
        TeacherInfoRequest updateRequest = TeacherInfoRequest.builder()
                .name("updatedName")
                .email("updatedEmail@gmail.com")
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
                .andExpect(jsonPath("$.name").value("updatedName"))
                .andExpect(jsonPath("$.email").value("updatedEmail@gmail.com"))
                .andExpect(jsonPath("$.brief_introduction").value("updatedBriefIntroduction"))
                .andExpect(jsonPath("$.academic_background").value("updatedAcademicBackground"))
                .andExpect(jsonPath("$.specialization").value("updatedSpecialization"));
    }
}
