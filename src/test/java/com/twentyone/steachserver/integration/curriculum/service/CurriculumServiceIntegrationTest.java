package com.twentyone.steachserver.integration.curriculum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.twentyone.steachserver.domain.auth.service.AuthService;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.error.DuplicatedCurriculumRegistrationException;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.member.repository.TeacherRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import com.twentyone.steachserver.integration.IntegrationTest;
import com.twentyone.steachserver.util.converter.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("커리큘럼 서비스 통합 테스트")
public class CurriculumServiceIntegrationTest extends IntegrationTest {
    public static final String TITLE = "title";
    public static final String SUB_TITLE = "subTitle";
    public static final String INTRO = "intro";
    public static final String INFORMATION = "information";
    public static final CurriculumCategory CURRICULUM_CATEGORY = CurriculumCategory.SOCIAL;
    public static final String SUB_CATEGORY = "subCategory";
    public static final String BANNER_IMG_URL = "bannerImgUrl";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String WEEKDAY_BITMASK = "0101011";
    public static final int MAX_ATTENDEES = 4;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
    private Pageable pageable;

    @BeforeEach
    void setUp() throws IOException {
        teacher = Teacher.of("teacher3132", "passw3ord132", "김범3식32", "bums33ik2@gmail.com", "imagePath");
        student = Student.of("student1332", "passwor3d232", "주효림323", "myli3m3e2@gmail.com");

        teacherRepository.save(teacher);
        studentRepository.save(student);

        this.pageable = PageRequest.of(0, 1000);
    }

    @Test
    @DisplayName("커리큘럼 추가")
    void create() {
        //given
        CurriculumAddRequest request = new CurriculumAddRequest(TITLE, SUB_TITLE, INTRO, INFORMATION, CURRICULUM_CATEGORY, SUB_CATEGORY, BANNER_IMG_URL,
                NOW.toLocalDate(), NOW.toLocalDate(), WEEKDAY_BITMASK, NOW.toLocalTime(), NOW.toLocalTime(), MAX_ATTENDEES);

        //when
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(teacher, request);

        assertEquals(curriculumDetailResponse.getTitle(), TITLE);
        assertEquals(curriculumDetailResponse.getSubTitle(), SUB_TITLE);
        assertEquals(curriculumDetailResponse.getIntro(), INTRO);
        assertEquals(curriculumDetailResponse.getInformation(), INFORMATION);
        assertEquals(curriculumDetailResponse.getCategory(), CURRICULUM_CATEGORY);
        assertEquals(curriculumDetailResponse.getSubCategory(), SUB_CATEGORY);
        assertEquals(curriculumDetailResponse.getBannerImgUrl(), BANNER_IMG_URL);
        assertEquals(DateTimeUtil.convert(request.getStartDate()), DateTimeUtil.convert(LocalDate.from(NOW)));
        assertEquals(DateTimeUtil.convert(request.getEndDate()), DateTimeUtil.convert(LocalDate.from(NOW)));
        assertEquals(curriculumDetailResponse.getWeekdaysBitmask(), WEEKDAY_BITMASK);
        assertEquals(DateTimeUtil.convert(request.getLectureStartTime()), DateTimeUtil.convert(LocalTime.from(NOW)));
        assertEquals(DateTimeUtil.convert(request.getLectureEndTime()), DateTimeUtil.convert(LocalTime.from(NOW)));
        assertEquals(curriculumDetailResponse.getMaxAttendees(), MAX_ATTENDEES);
    }

    @Disabled
    @Test
    void 수강신청() {
        //given
        CurriculumAddRequest request = new CurriculumAddRequest(TITLE, SUB_TITLE, INTRO, INFORMATION,
                CURRICULUM_CATEGORY, SUB_CATEGORY, BANNER_IMG_URL,
                NOW.toLocalDate(), NOW.toLocalDate(), WEEKDAY_BITMASK, NOW.toLocalTime(), NOW.toLocalTime(), MAX_ATTENDEES);
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(teacher, request);
        Integer curriculumId = curriculumDetailResponse.getCurriculumId();

        //when
        curriculumService.registration(student, curriculumId);

        CurriculumListResponse studentsCurricula = curriculumService.getStudentsCurricula(student, pageable);
        CurriculumDetailResponse response = studentsCurricula.getCurricula().get(0);

        //then
        assertEquals(response.getCurriculumId(), curriculumId);
    }

    static Stream<Arguments> generateWeekDaysCompletedData() {
        return Stream.of(
                //[2024-07-01 ~ 2024-07-01] 1000000 => 2024-07-01
                Arguments.of(
                        getLocalDateTime("2024-07-01"),
                        getLocalDateTime("2024-07-01"),
                        "1000000",
                        List.of(new LocalDateTime[]{getLocalDateTime("2024-07-01")})
                ),
                //[2024-07-03 ~ 2024-07-10] 0010100 => 2024-07-03, 2024-07-05, 2024-07-10, 2024-07-12
                Arguments.of(
                        getLocalDateTime("2024-07-03"),
                        getLocalDateTime("2024-07-12"),
                        "0010100",
                        List.of(new LocalDateTime[]{
                                getLocalDateTime("2024-07-03"),
                                getLocalDateTime("2024-07-05"),
                                getLocalDateTime("2024-07-10"),
                                getLocalDateTime("2024-07-12")
                        })
                ),
                //[2024-07-25 ~ 2024-08-03] 0001110 => 2024-07-25, 2024-07-26, 2024-07-27, 2024-08-01, 2024-08-02, 2024-08-03
                Arguments.of(
                        getLocalDateTime("2024-07-25"),
                        getLocalDateTime("2024-08-03"),
                        "0001110",
                        List.of(new LocalDateTime[]{
                                getLocalDateTime("2024-07-25"),
                                getLocalDateTime("2024-07-26"),
                                getLocalDateTime("2024-07-27"),
                                getLocalDateTime("2024-08-01"),
                                getLocalDateTime("2024-08-02"),
                                getLocalDateTime("2024-08-03"),
                        })
                ),
                //[2024-12-25 ~ 2025-01-04] 0010011 => 2024-12-25, 2024-12-28, 2024-12-29, 2025-01-01, 2025-01-04
                Arguments.of(
                        getLocalDateTime("2024-12-25"),
                        getLocalDateTime("2025-01-04"),
                        "0010011",
                        List.of(new LocalDateTime[]{
                                getLocalDateTime("2024-12-25"),
                                getLocalDateTime("2024-12-28"),
                                getLocalDateTime("2024-12-29"),
                                getLocalDateTime("2025-01-01"),
                                getLocalDateTime("2025-01-04")
                        })
                )
        );
    }

    private static LocalDateTime getLocalDateTime(String text) {
        return LocalDate.parse(text, DATE_TIME_FORMATTER).atStartOfDay();
    }

    @ParameterizedTest
    @MethodSource("generateWeekDaysCompletedData")
    @DisplayName("주간 수강신청")
    void getSelectedWeekdaysCompleted(LocalDateTime startDate, LocalDateTime endDate, String weekDayBitmask,
                                      List<LocalDateTime> lectureStartTime) {
        //given

        //when
        List<LocalDateTime> selectedWeekdays = curriculumService.getSelectedWeekdays(startDate, endDate,
                (byte) Integer.parseInt(weekDayBitmask, 2));

        //then
        assertEquals(lectureStartTime.size(), selectedWeekdays.size());

        for (int i = 0; i < lectureStartTime.size(); i++) {
            assertEquals(lectureStartTime.get(i), selectedWeekdays.get(i));
        }
    }

    @Test
    void 여러번_수강신청_불가능() {
        //given
        CurriculumAddRequest request = new CurriculumAddRequest(TITLE, SUB_TITLE, INTRO, INFORMATION,
                CURRICULUM_CATEGORY, SUB_CATEGORY, BANNER_IMG_URL,
                NOW.toLocalDate(), NOW.toLocalDate(), WEEKDAY_BITMASK, NOW.toLocalTime(), NOW.toLocalTime(), MAX_ATTENDEES);
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(teacher, request);
        Integer curriculumId = curriculumDetailResponse.getCurriculumId();

        curriculumService.registration(student, curriculumId);

        //when //then
        assertThrows(
                DuplicatedCurriculumRegistrationException.class,
                () -> {
                    curriculumService.registration(student, curriculumId);
                }
        );
    }
}
