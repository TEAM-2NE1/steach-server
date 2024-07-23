package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumDetailRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumSearchRepository;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.studentCurriculum.repository.StudentCurriculumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CurriculumServiceImplTest {
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

    @Mock
    private CurriculumRepository curriculumRepository;

    @Mock
    private CurriculumSearchRepository curriculumSearchRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private CurriculumDetailRepository curriculumDetailRepository;

    @Mock
    private StudentCurriculumRepository studentCurriculumRepository;

    @Test
    void create() {
        Teacher teacher = Teacher.of(USERNAME, PASSWORD, NAME, EMAIL, PATH);
        CurriculumAddRequest request = new CurriculumAddRequest(TITLE, SUB_TITLE, INTRO, INFORMATION, CURRICULUM_CATEGORY, SUB_CATEGORY, BANNER_IMG_URL,
                NOW, NOW, WEEKDAY_BITMASK, NOW, NOW, MAX_ATTENDEES);

        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(teacher, request);
        assertEquals(curriculumDetailResponse.getTitle(), TITLE);
        assertEquals(curriculumDetailResponse.getSubTitle(), SUB_TITLE);
        assertEquals(curriculumDetailResponse.getIntro(), INTRO);
        assertEquals(curriculumDetailResponse.getInformation(), INFORMATION);
        assertEquals(curriculumDetailResponse.getCategory(), CURRICULUM_CATEGORY);
        assertEquals(curriculumDetailResponse.getSubCategory(), SUB_CATEGORY);
        assertEquals(curriculumDetailResponse.getBannerImgUrl(), BANNER_IMG_URL);
        assertEquals(curriculumDetailResponse.getStartDate(), NOW.toLocalDate());
        assertEquals(curriculumDetailResponse.getEndDate(), NOW.toLocalDate());
        assertEquals(curriculumDetailResponse.getWeekdaysBitmask(), WEEKDAY_BITMASK);
        assertEquals(curriculumDetailResponse.getLectureStartTime(), NOW);
        assertEquals(curriculumDetailResponse.getLectureEndTime(), NOW);
        assertEquals(curriculumDetailResponse.getMaxAttendees(), MAX_ATTENDEES);
    }
}