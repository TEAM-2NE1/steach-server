package com.twentyone.steachserver.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInstructorFunctions() throws Exception {
        String uniqueUsername = "instructor_" + UUID.randomUUID();

        // 아이디 중복 확인
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/check-username/" + uniqueUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.can_use").value(true));  // 먼저 사용 가능한지 확인합니다.

        // 강사 회원가입
        TeacherSignUpDto teacherSignUpDto = TeacherSignUpDto.builder()
                .username(uniqueUsername)
                .password("pass123")
                .name("Instructor Name") // 필수 필드 설정
                .email("instructor1@example.com")
                .build();

        MockMultipartFile file = new MockMultipartFile("file", "resume.txt", "text/plain", "dummy content".getBytes());
        MockMultipartFile teacherSignUpDtoJson = new MockMultipartFile("teacherSignUpDto", "", "application/json", objectMapper.writeValueAsBytes(teacherSignUpDto));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/teacher/join")
                        .file(file)
                        .file(teacherSignUpDtoJson))
                .andExpect(status().isCreated());

        // 로그인
        LoginDto loginDto = LoginDto.builder()
                .username(uniqueUsername)
                .password("pass123")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());

        // 아이디 중복 확인 (이미 사용 중인 닉네임임을 확인합니다)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/check-username/" + uniqueUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.can_use").value(false));
    }


    // 2. 강사 커리큘럼 조회 및 강의 목록 조회
    @WithMockUser(roles = "TEACHER")
    @Test
    public void testInstructorCurriculumInquiryAndLectureList() throws Exception {
        // 선생님이 강의하는 커리큘럼 조회
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers/curricula"))
                .andExpect(status().isOk());

        // 강의 목록 조회 (92개의 강의가 있다고 가정)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(92));
    }

    // 예시





    // 3. 커리큘럼 생성
//    @WithMockUser(roles = "TEACHER")
//    @Test
//    public void testCreatingCurriculum() throws Exception {
//        CurriculumAddRequest curriculumRequest = CurriculumAddRequest();
//        curriculumRequest.("New Curriculum");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/curricula")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(curriculumRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(34));
//    }
//
//    // 4. 퀴즈 관련 기능 테스트
//    @WithMockUser(roles = "TEACHER")
//    @Test
//    public void testQuizFunctions() throws Exception {
//        // 퀴즈 생성
//        QuizRequest quizRequest = new QuizRequest();
//        quizRequest.setTitle("New Quiz");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/quizzes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(quizRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(20));
//
//        // 퀴즈 조회
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/quizzes"))
//                .andExpect(status().isOk());
//    }
//
//    // 5. 인증 코드 발급 및 검증
//    @WithMockUser(roles = "ADMIN")
//    @Test
//    public void testAuthCodeFunctions() throws Exception {
//        // 인증 코드 발급
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/code"))
//                .andExpect(status().isOk());
//
//        // 인증 코드 검증
//        AuthCodeValidationRequest validationRequest = new AuthCodeValidationRequest();
//        validationRequest.setCode("123456");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/code/validate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(validationRequest)))
//                .andExpect(status().isOk());
//    }
//
//    // 6. Admin 인증 코드 발급 및 검증
//    @WithMockUser(roles = "ADMIN")
//    @Test
//    public void testAdminAuthCodeFunctions() throws Exception {
//        // 인증 코드 발급
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/auth/code"))
//                .andExpect(status().isOk());
//
//        // 인증 코드 검증
//        AuthCodeValidationRequest validationRequest = new AuthCodeValidationRequest();
//        validationRequest.setCode("123456");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/auth/code/validate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(validationRequest)))
//                .andExpect(status().isOk());
//    }
//
//    // 7. 학생 관련 기능 테스트
//    @Test
//    public void testStudentFunctions() throws Exception {
//        // 학생 회원가입
//        StudentSignUpDto studentSignUpDto = new StudentSignUpDto();
//        studentSignUpDto.setUsername("student1");
//        studentSignUpDto.setPassword("pass123");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student/join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(studentSignUpDto)))
//                .andExpect(status().isCreated());
//
//        // 로그인
//        LoginDto loginDto = new LoginDto("student1", "pass123");
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginDto)))
//                .andExpect(status().isOk());
//
//        // 아이디 중복 확인
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/check-username/student1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.available").value(false));
//    }
//
//    // 8. Teacher의 Curriculum 관련 기능 테스트
//    @WithMockUser(roles = "TEACHER")
//    @Test
//    public void testTeacherCurriculumFunctions() throws Exception {
//        // 커리큘럼 생성
//        CurriculumRequest curriculumRequest = new CurriculumRequest();
//        curriculumRequest.setName("Teacher Curriculum");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/curricula")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(curriculumRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(34));
//
//        // 선생님의 커리큘럼 조회
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers/curricula"))
//                .andExpect(status().isOk());
//
//        // 강의 시작 - 교실 열기
//        ClassroomRequest classroomRequest = new ClassroomRequest();
//        classroomRequest.setClassroomId("101");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/classrooms/open")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(classroomRequest)))
//                .andExpect(status().isOk());
//
//        // 강의 시작
//        LectureRequest lectureRequest = new LectureRequest();
//        lectureRequest.setLectureId("1001");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lectures/start")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(lectureRequest)))
//                .andExpect(status().isOk());
//    }
//
//    // 9. 학생의 Lecture 관련 기능 테스트
//    @WithMockUser(roles = "STUDENT")
//    @Test
//    public void testStudentLectureFunctions() throws Exception {
//        // 강의 참석 여부 확인
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lectures/attendance?studentId=student1&lectureId=1001"))
//                .andExpect(status().isOk());
//
//        // 퀴즈 풀기
//        QuizAttemptRequest quizAttemptRequest = new QuizAttemptRequest();
//        quizAttemptRequest.setStudentId("student1");
//        quizAttemptRequest.setQuizId("20");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/quizzes/attempt")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(quizAttemptRequest)))
//                .andExpect(status().isOk());
//        // Send concentration
//        mockMvc.perform(MockMvcRequestBuilders.post("/lecture/send-concentration")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"studentId\": \"student1\", \"lectureId\": \"1001\" }"))
//                .andExpect(status().isOk());
//    }
//
//    // 10. Teacher의 Lecture 종료
//    @Test
//    public void testEndOfLecture() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/lecture/end")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"lectureId\": \"1001\" }"))
//                .andExpect(status().isOk());
//    }
//
//    // 11. GTP career recommendation
//    @Test
//    public void testGTPCareerRecommendation() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/gtp/career-recommendation?studentId=student1"))
//                .andExpect(status().isOk());
//    }
//
//    // 12. Radar chart search
//    @Test
//    public void testRadarChartSearch() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/chart/radar-search?studentId=student1"))
//                .andExpect(status().isOk());
//    }
//
//    // 13. Curriculum search
//    @Test
//    public void testCurriculumSearch() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/curriculum/search?query=advanced"))
//                .andExpect(status().isOk());
//    }
}
