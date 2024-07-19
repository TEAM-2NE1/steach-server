package com.twentyone.steachserver.domain.studentLecture.service;

import com.twentyone.steachserver.domain.studentLecture.dto.StudentLectureStatisticDto;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;

import java.util.Optional;

public interface StudentLectureService {
    Optional<StudentLecture> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);

    void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime);

    void updateStudentLectureByFinishLecture(Integer lectureId);

    StudentLectureStatisticDto createLectureStudentStatistic(Integer lectureId);
}
