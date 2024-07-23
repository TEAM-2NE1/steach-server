package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.service.AuthService;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.member.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class CurriculumServiceIntegrationTest {
    public static final String TITLE = "title";
    public static final String SUB_TITLE = "subTitle";
    public static final String INTRO = "intro";
    public static final String INFORMATION = "information";
    public static final CurriculumCategory CURRICULUM_CATEGORY = CurriculumCategory.EDUCATION;
    public static final String SUB_CATEGORY = "subCategory";
    public static final String BANNER_IMG_URL = "bannerImgUrl";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String WEEKDAY_BITMASK = "0101011";
    public static final int MAX_ATTENDEES = 4;

    @Autowired
    CurriculumService curriculumService;

    @Autowired
    AuthService authService;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() throws IOException {
        teacher = Teacher.of("teacher3132", "passw3ord132", "김범3식32", "bums33ik2@gmail.com", "imagePath");
        student = Student.of("student1332", "passwor3d232", "주효림323", "myli3m3e2@gmail.com");

        teacherRepository.save(teacher);
        studentRepository.save(student);
    }

    @Test
    void 수강신청() {
        //given
        CurriculumAddRequest request = new CurriculumAddRequest(TITLE, SUB_TITLE, INTRO, INFORMATION, CURRICULUM_CATEGORY, SUB_CATEGORY, BANNER_IMG_URL,
                NOW, NOW, WEEKDAY_BITMASK, NOW, NOW, MAX_ATTENDEES);
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(teacher, request);
        Integer curriculumId = curriculumDetailResponse.getCurriculumId();

        //when
        curriculumService.registration(student, curriculumId);

        CurriculumListResponse studentsCurricula = curriculumService.getStudentsCurricula(student);
        CurriculumDetailResponse response = studentsCurricula.getCurricula().get(0);

        //then
        assertEquals(response.getCurriculumId(), curriculumId);
    }
}
