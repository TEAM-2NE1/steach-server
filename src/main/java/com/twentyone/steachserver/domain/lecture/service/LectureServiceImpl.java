package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.dto.*;
import com.twentyone.steachserver.domain.lecture.dto.update.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureQueryRepository;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;

import com.twentyone.steachserver.domain.lecture.validator.LectureValidator;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureRepository;
import com.twentyone.steachserver.global.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;
    private final LectureQueryRepository lectureQueryRepository;
    private final StudentRepository studentRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;
    private final StudentLectureRepository studentLectureRepository;

    private final LectureValidator lectureValidator;

    @Override
    public List<Lecture> upcomingLecture(int fromMinute, int toMinute) {
        LocalDateTime fromTime = LocalDateTime.now().plusMinutes(fromMinute);
        LocalDateTime toTime = LocalDateTime.now().plusMinutes(toMinute);

        return lectureRepository.findByLectureStartDateBetween(fromTime, toTime);
    }

    @Override
    public LectureBeforeStartingResponseDto getLectureInformation(Integer lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));

        LectureBeforeStartingResponseDto lectureBeforeStartingResponse = lectureQueryRepository.getLectureBeforeStartingResponse(
                lectureId);

        if (lecture.getRealEndTime() == null) {
            return lectureBeforeStartingResponse;
        }

        List<StudentInfoByLectureDto> studentInfoByLecture = studentLectureQueryRepository.getStudentInfoByLecture(
                lectureId);
        lectureBeforeStartingResponse.completeLecture();

        return CompletedLecturesResponseDto.of(lectureBeforeStartingResponse, studentInfoByLecture, lecture);
    }

    @Override
    @Transactional
    public Optional<LectureBeforeStartingResponseDto> updateLectureInformation(Integer lectureId,
                                                                               UpdateLectureRequestDto lectureRequestDto) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));

        lecture.update(lectureRequestDto.lectureTitle(), lectureRequestDto.lectureStartTime(), lectureRequestDto.lectureEndTime());
        return Optional.ofNullable(lectureQueryRepository.getLectureBeforeStartingResponse(lectureId));
    }

    @Override
    @Transactional
    public Lecture updateRealEndTime(Integer lectureId) {
        return lectureRepository.findById(lectureId)
                .map(lecture -> {
                    LocalDateTime realStartTime = lecture.getRealStartTime();
                    System.out.println(realStartTime);
                    if (realStartTime == null) {
                        throw new IllegalArgumentException("lecture not started, can't update real end time");
                    }
                    LocalDateTime realEndTime = lecture.getRealEndTime();
                    if (realEndTime == null) {
                        lecture.updateRealEndTimeWithNow();
                    } else {
                        throw new IllegalArgumentException("lecture already ended, can't update real end time again");
                    }
                    return lecture;
                })
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));
    }

    @Override
    @Transactional
    public void updateRealStartTime(Integer lectureId) {
        lectureRepository.findById(lectureId)
                .ifPresentOrElse(
                        lecture -> {
                            LocalDateTime realStartTime = lecture.getRealStartTime();
                            if (realStartTime == null) {
                                lecture.updateRealStartTimeWithNow();
                            } else {
                                throw new IllegalArgumentException("lecture already started");
                            }
                        },
                        () -> {
                            throw new IllegalArgumentException("lecture not found");
                        }
                );
    }

    @Override
    public AllLecturesInCurriculaResponseDto findByCurriculum(Integer curriculumId) {
        List<Lecture> lectures = lectureRepository.findByCurriculumId(curriculumId)
                .orElseGet(() -> new ArrayList<>());

//        List<LectureResponseDto> list = LectureResponseDto.fromDomainList(lectures);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        Map<Integer, List<LectureResponseDto>> lecturesByWeek = new HashMap<>();

        //주단위로 나누기
        int currentWeekIdx = 0;
        int prev = -1;
        for (Lecture lecture : lectures) {
            LocalDate startDate = lecture.getLectureStartDate().toLocalDate();
            int weekNumber = startDate.get(weekFields.weekOfWeekBasedYear());
            if (weekNumber != prev) {
                prev = weekNumber;
                List<LectureResponseDto> value = new ArrayList<>();
                value.add(LectureResponseDto.fromDomain(lecture));
                currentWeekIdx++;

                lecturesByWeek.put(currentWeekIdx, value);
            } else {
                List<LectureResponseDto> lectureResponseDtoList = lecturesByWeek.get(currentWeekIdx);
                lectureResponseDtoList.add(LectureResponseDto.fromDomain(lecture));
            }
        }

        return AllLecturesInCurriculaResponseDto.of(lecturesByWeek, lectures.size());
    }

    @Override
    public void addVolunteerMinute(Lecture updateLecture) {
        Integer lectureOrder = updateLecture.getLectureOrder();
        Curriculum curriculum = updateLecture.getCurriculum();

        int curriculumLectureSize = curriculum.getLectures().size();

        if (curriculumLectureSize == lectureOrder) {
            int volunteerMinute = 0;

            for (Lecture lecture : curriculum.getLectures()) {
                LocalDateTime realStartTime = lecture.getRealStartTime();
                LocalDateTime realEndTime = lecture.getRealEndTime();

                if (realStartTime != null && realEndTime != null) {
                    int lectureDurationMinutes = Math.toIntExact(Duration.between(realStartTime, realEndTime).toMinutes());
                    volunteerMinute += lectureDurationMinutes;
                }
            }
            curriculum.getTeacher().updateVolunteerMinute(volunteerMinute);
        }
    }

    @Override
    @Transactional
    public void delete(Integer lectureId, Teacher teacher) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("lecture not found"));

        if (!lecture.getCurriculum().getTeacher().equals(teacher)) {
            throw new ForbiddenException("커리큘럼을 만든 사람만 수정이 가능. 권한이 없음");
        }

        lectureRepository.delete(lecture);
    }

    @Override
    public Boolean checkStudentByLecture(Integer studentId, Integer lectureId) {
        Optional<StudentLecture> byStudentIdAndLectureId = studentLectureRepository.findByStudentIdAndLectureId(studentId, lectureId);
        return byStudentIdAndLectureId.isPresent();
    }

    @Override
    public Optional<Classroom> getClassroomByLectureAndStudent(Integer studentId, Integer lectureId) {
        Lecture lecture = lectureRepository.getReferenceById(lectureId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("student not found"));
        lectureValidator.validateLectureOfLectureAuth(lecture, student);

        return lectureQueryRepository.findClassroomByLectureAndStudent(lectureId, studentId);
    }

    @Override
    public FinalLectureInfoByTeacherDto getFinalLectureInformation(Integer lectureId) {
        List<StudentInfoByLectureDto> studentInfoByLecture = studentLectureQueryRepository.getStudentInfoByLecture(lectureId);

        return FinalLectureInfoByTeacherDto.createFinalLectureInfoByTeacherDto(studentInfoByLecture);
    }

    @Override
    public CompletedLecturesResponseDto getFinalLectureInformation(
            LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto, Integer lectureId) {

        List<StudentInfoByLectureDto> studentInfoByLecture = studentLectureQueryRepository.getStudentInfoByLecture(lectureId);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));
        return CompletedLecturesResponseDto.of(lectureBeforeStartingResponseDto, studentInfoByLecture, lecture);
    }
}

