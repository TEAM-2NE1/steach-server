package com.twentyone.steachserver.unit.domain.curriculum.service;

import com.twentyone.steachserver.SteachTest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumServiceImpl;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("커리큘럼 서비스 단위테스트")
class CurriculumServiceImplTest extends SteachTest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PATH = "path";
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

    @InjectMocks
    CurriculumServiceImpl curriculumService;

    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() {
        teacher = Teacher.of(USERNAME, PASSWORD, NAME, EMAIL, PATH);
        student = Student.of(USERNAME, PASSWORD, NAME, EMAIL);
    }

    @Test
    void create_선생님만_가능() {
        //given
        CurriculumAddRequest request = new CurriculumAddRequest(TITLE, SUB_TITLE, INTRO, INFORMATION, CURRICULUM_CATEGORY, SUB_CATEGORY, BANNER_IMG_URL,
                NOW.toLocalDate(), NOW.toLocalDate(), WEEKDAY_BITMASK, NOW.toLocalTime(), NOW.toLocalTime(), MAX_ATTENDEES);

//        Fixme: 권한관련 처리는 controller에서 했으니 service에서 처리해주는거보다 controller 단에서 처리해주는게 좋아보입니다.
        // 추가로 이 부분이 실패처리됩니다.
        //when //then
        assertThrows(RuntimeException.class, () -> {
            curriculumService.create(student, request);
        });
    }
}
