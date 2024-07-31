package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.lecture.dto.*;
import com.twentyone.steachserver.domain.lecture.dto.update.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Teacher;

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

    WeekLectureListResponseDto findByCurriculum(Integer curriculumId);

    void addVolunteerMinute(Lecture updateLecture);

    void delete(Integer lectureId, Teacher teacher);
}
