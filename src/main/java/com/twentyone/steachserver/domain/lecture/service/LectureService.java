package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.lecture.dto.CompletedLecturesResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureListResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.update.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureService {
    LectureBeforeStartingResponseDto getLectureInformation(Integer lectureId);

    FinalLectureInfoByTeacherDto getFinalLectureInformation(Integer lectureId);

    CompletedLecturesResponseDto getFinalLectureInformation(
            LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto, Integer lectureId);

    Optional<Classroom> getClassroomByLectureAndStudent(Integer studentId, Integer lectureId);

    Boolean checkStudentByLecture(Integer studentId, Integer lectureId);

    List<Lecture> upcomingLecture(int toMinute, int fromMinute);

    Optional<LectureBeforeStartingResponseDto> updateLectureInformation(Integer lectureId,
                                                                        UpdateLectureRequestDto lectureRequestDto);

    Lecture updateRealEndTime(Integer lectureId);

    void updateRealStartTime(Integer lectureId);

    LectureListResponseDto findByCurriculum(Integer curriculumId);
}
