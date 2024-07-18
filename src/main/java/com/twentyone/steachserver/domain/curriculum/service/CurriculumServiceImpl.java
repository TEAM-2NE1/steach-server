package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumDetailRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.curriculum.repository.StudentCurriculumRepository;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final LectureRepository lectureRepository;
    private final CurriculumDetailRepository curriculumDetailRepository;
    private final StudentCurriculumRepository studentCurriculumRepository;

    @Override
    @Transactional(readOnly = true)
    public CurriculumDetailResponse getDetail(Integer id) {
        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curriculum not found"));

        return CurriculumDetailResponse.fromDomain(curriculum);
    }

    @Override
    @Transactional
    public CurriculumDetailResponse create(LoginCredential loginCredential, CurriculumAddRequest request) {
        //Teacher 인지 학인
        if (!(loginCredential instanceof Teacher)) {
            throw new RuntimeException("선생님만 만들 수 있습니다.");
        }

        //bitmask byte로 변환
        //이진수 문자열을 정수로 변환
        Byte weekdaysBitmask = bitmaskStringToByte(request.getWeekdaysBitmask());

        //detail 만들기
        CurriculumDetail curriculumDetail = CurriculumDetail.builder()
                .subTitle(request.getSubTitle())
                .intro(request.getIntro())
                .subCategory(request.getSubCategory())
                .information(request.getInformation())
                .bannerImgUrl(request.getBannerImgUrl())
                .weekdaysBitmask(weekdaysBitmask)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .lectureStartTime(request.getLectureStartTime())
                .lectureCloseTime(request.getLectureEndTime())
                .maxAttendees(request.getMaxAttendees())
                .build();
        curriculumDetailRepository.save(curriculumDetail);

        Curriculum curriculum = Curriculum.of(request.getTitle(), request.getCategory(), (Teacher) loginCredential, curriculumDetail);
        curriculumRepository.save(curriculum);

        //lecture 만들기
        //날짜 오름차순대로 들어감
        List<LocalDate> selectedDates = getSelectedWeekdays(request.getStartDate(), request.getEndDate(), weekdaysBitmask);

        for (int i = 0; i < selectedDates.size(); i++) {
            LocalDate lectureDate = selectedDates.get(i);
            int order = i + 1;
            Lecture lecture = Lecture.of(request.getTitle() + " " + order + "강", i, lectureDate.atStartOfDay(), request.getLectureStartTime(), request.getLectureEndTime(), curriculum);
            lectureRepository.save(lecture);
        }

        return CurriculumDetailResponse.fromDomain(curriculum); //관련 강의도 줄까?? 고민
    }

    @Override
    @Transactional
    public void registration(LoginCredential loginCredential, Integer curriculaId) {
        if (!(loginCredential instanceof Student)) {
            throw new RuntimeException("학생만 수강신청이 가능합니다.");
        }

        Student student = (Student) loginCredential;

        Curriculum curriculum = curriculumRepository.findByIdWithLock(curriculaId)
                .orElseThrow(() -> new RuntimeException("찾을 수 없음"));

        CurriculumDetail curriculumDetail = curriculum.getCurriculumDetail();

        if (curriculumDetail.getMaxAttendees() <= curriculumDetail.getCurrentAttendees()) {
            throw new RuntimeException("수강정원이 다 찼습니다.");
        }

        StudentCurriculum studentCurriculum = new StudentCurriculum(student, curriculum);
        studentCurriculumRepository.save(studentCurriculum);

        curriculum.register();
    }

    @Override
    @Transactional(readOnly = true)
    public CurriculumListResponse getMyCourses(LoginCredential credential) {
        if (credential instanceof Student student) {
            return getStudentCourses(student);
        } else if (credential instanceof Teacher teacher) {
            return getTeacherCourses(teacher);
        } else {
            throw new RuntimeException("에러");
        }
    }

    private CurriculumListResponse getTeacherCourses(Teacher teacher) {
        List<Curriculum> curriculumList = curriculumRepository.findAllByTeacher(teacher)
                .orElseGet(() -> new ArrayList<>());

        return CurriculumListResponse.fromDomainList(curriculumList);
    }

    private CurriculumListResponse getStudentCourses(Student student) {
        List<StudentCurriculum> studentsCurricula = studentCurriculumRepository.findByStudent(student)
                .orElseGet(() -> new ArrayList<>());

        List<Curriculum> curriculaList = new ArrayList<>();
        for (StudentCurriculum studentCurriculum : studentsCurricula) {
            curriculaList.add(studentCurriculum.getCurriculum());
        }

        return CurriculumListResponse.fromDomainList(curriculaList);
    }

    private byte bitmaskStringToByte(String bitmaskString) {
        if (bitmaskString.length() != 7) {
            throw new RuntimeException("bitmask 이상함");
        }

        if (!bitmaskString.matches("[01]*")) {
            throw new RuntimeException("bitmask 이상함");
        }

        return (byte) Integer.parseInt(bitmaskString, 2);
    }

    public List<LocalDate> getSelectedWeekdays(LocalDate startDate, LocalDate endDate, int weekdaysBitmask) {
        List<LocalDate> selectedDates = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            int dayOfWeekValue = getBitmaskForDayOfWeek(dayOfWeek);

            if ((weekdaysBitmask & dayOfWeekValue) != 0) {
                selectedDates.add(date);
            }
        }

        return selectedDates;
    }

    private int getBitmaskForDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return 64; // 1000000
            case TUESDAY:
                return 32; // 0100000
            case WEDNESDAY:
                return 16; // 0010000
            case THURSDAY:
                return 8;  // 0001000
            case FRIDAY:
                return 4;  // 0000100
            case SATURDAY:
                return 2;  // 0000010
            case SUNDAY:
                return 1;  // 0000001
            default:
                return 0;
        }
    }
}
