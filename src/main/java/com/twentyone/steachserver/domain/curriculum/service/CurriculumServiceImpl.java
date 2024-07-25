package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculaSearchCondition;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.error.DuplicatedCurriculumRegistrationException;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumDetailRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumSearchRepository;
import com.twentyone.steachserver.domain.curriculum.validator.CurriculumValidator;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.repository.StudentCurriculumRepository;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final CurriculumSearchRepository curriculumSearchRepository;
    private final LectureRepository lectureRepository;
    private final CurriculumDetailRepository curriculumDetailRepository;
    private final StudentCurriculumRepository studentCurriculumRepository;
    private final StudentLectureRepository studentLectureRepository;

    private final CurriculumValidator curriculumValidator;

    @Override
    @Transactional(readOnly = true)
    public CurriculumDetailResponse getDetail(Integer id) {
        Curriculum curriculum = curriculumRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new RuntimeException("Curriculum not found"));

        return CurriculumDetailResponse.fromDomain(curriculum);
    }

    @Override
    @Transactional
    public CurriculumDetailResponse create(LoginCredential loginCredential, CurriculumAddRequest request) {
        //Teacher 인지 학인
        if (!(loginCredential instanceof Teacher)) {
            throw new ForbiddenException("선생님만 만들 수 있습니다.");
        }
        curriculumValidator.validatorMaxAttendees(request);
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
                .startDate(LocalDate.from(request.getStartDate()))
                .endDate(LocalDate.from(request.getEndDate()))
                .lectureStartTime(request.getLectureStartTime())
                .lectureCloseTime(request.getLectureEndTime())
                .maxAttendees(request.getMaxAttendees())
                .build();
        curriculumDetailRepository.save(curriculumDetail);

        Curriculum curriculum = Curriculum.of(request.getTitle(), request.getCategory(), (Teacher) loginCredential,
                curriculumDetail);
        curriculumRepository.save(curriculum);

        //lecture 만들기
        //날짜 오름차순대로 들어감
        List<LocalDateTime> selectedDates = getSelectedWeekdays(request.getStartDate(), request.getEndDate(),
                weekdaysBitmask);

        for (int i = 0; i < selectedDates.size(); i++) {
            LocalDateTime lectureDate = selectedDates.get(i).with(curriculumDetail.getLectureStartTime()); // 날짜에 시간 설정
            int order = i + 1;
            Lecture lecture = Lecture.of(request.getTitle() + " " + order + "강", order,
                    lectureDate, curriculum);
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

        if (curriculum.getCurriculumDetail().getMaxAttendees() <= curriculum.getCurriculumDetail()
                .getCurrentAttendees()) {
            throw new RuntimeException("수강정원이 다 찼습니다.");
        }

        studentCurriculumRepository.findTop1ByStudentAndCurriculum(student, curriculum)
                .ifPresent(sc -> {
                    throw new DuplicatedCurriculumRegistrationException("중복 수강신청 불가능");
                });

        StudentCurriculum studentCurriculum = new StudentCurriculum(student, curriculum);

        if (curriculum.getLectures() != null) {
            for (Lecture lecture : curriculum.getLectures()) {
                studentLectureRepository.save(StudentLecture.of(student, lecture));
            }
        }

        studentCurriculumRepository.save(studentCurriculum);

        curriculum.register();
    }

    @Override
    @Transactional(readOnly = true)
    public CurriculumListResponse getTeachersCurricula(Teacher teacher) {
        List<Curriculum> curriculumList = curriculumRepository.findAllByTeacher(teacher)
                .orElseGet(ArrayList::new);

        return CurriculumListResponse.fromDomainList(curriculumList);
    }

    @Override
    @Transactional(readOnly = true)
    public CurriculumListResponse getStudentsCurricula(Student student) {
        List<StudentCurriculum> studentsCurricula = studentCurriculumRepository.findByStudent(student)
                .orElseGet(ArrayList::new);

        List<Curriculum> curriculaList = new ArrayList<>();
        for (StudentCurriculum studentCurriculum : studentsCurricula) {
            curriculaList.add(studentCurriculum.getCurriculum());
        }

        return CurriculumListResponse.fromDomainList(curriculaList);
    }

    @Override
    public CurriculumListResponse search(CurriculaSearchCondition condition, Pageable pageable) {
        Page<Curriculum> curriculumList = curriculumSearchRepository.search(condition, pageable);

        return CurriculumListResponse.fromDomainList(curriculumList);
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

    @Override
    public List<LocalDateTime> getSelectedWeekdays(LocalDateTime startDate, LocalDateTime endDate,
                                                   int weekdaysBitmask) {
        List<LocalDateTime> selectedDates = new ArrayList<>();

        for (LocalDateTime date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            int dayOfWeekValue = getBitmaskForDayOfWeek(dayOfWeek);

            if ((weekdaysBitmask & dayOfWeekValue) != 0) {
                selectedDates.add(date);
            }
        }

        return selectedDates;
    }

    private int getBitmaskForDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> 64; // 1000000
            case TUESDAY -> 32; // 0100000
            case WEDNESDAY -> 16; // 0010000
            case THURSDAY -> 8;  // 0001000
            case FRIDAY -> 4;  // 0000100
            case SATURDAY -> 2;  // 0000010
            case SUNDAY -> 1;  // 0000001
            default -> 0;
        };
    }
}
