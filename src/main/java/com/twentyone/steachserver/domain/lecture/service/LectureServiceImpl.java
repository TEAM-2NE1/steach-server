package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.lecture.dto.*;
import com.twentyone.steachserver.domain.lecture.dto.update.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureQueryRepository;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;

import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureQueryRepository lectureQueryRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;

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
    public Optional<LectureBeforeStartingResponseDto> updateLectureInformation(Integer lectureId,
                                                                               UpdateLectureRequestDto lectureRequestDto) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));

        lecture.update(lectureRequestDto);
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
    public LectureListResponseDto findByCurriculum(Integer curriculumId) {
        List<Lecture> lectures = lectureRepository.findByCurriculumId(curriculumId)
                .orElseGet(() -> new ArrayList<>());

        return LectureListResponseDto.fromDomainList(lectures);
    }

    @Override
    public Optional<Classroom> getClassroomByLectureAndStudent(Integer studentId, Integer lectureId) {
        Optional<Classroom> classroomByLectureAndStudent = lectureQueryRepository.findClassroomByLectureAndStudent(
                lectureId, studentId);
        System.out.println(classroomByLectureAndStudent);
        return classroomByLectureAndStudent;
    }


    @Override
    public FinalLectureInfoByTeacherDto getFinalLectureInformation(Integer lectureId) {
        List<StudentInfoByLectureDto> studentInfoByLecture = studentLectureQueryRepository.getStudentInfoByLecture(
                lectureId);
        return FinalLectureInfoByTeacherDto.createFinalLectureInfoByTeacherDto(studentInfoByLecture);
    }

    @Override
    public CompletedLecturesResponseDto getFinalLectureInformation(
            LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto, Integer lectureId) {
        List<StudentInfoByLectureDto> studentInfoByLecture = studentLectureQueryRepository.getStudentInfoByLecture(
                lectureId);
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));
        return CompletedLecturesResponseDto.of(lectureBeforeStartingResponseDto, studentInfoByLecture, lecture);
    }

}

