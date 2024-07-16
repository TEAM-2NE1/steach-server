package com.twentyone.steachserver.domain.lectureStudents.service;

import com.twentyone.steachserver.domain.lectureStudents.model.LecturesStudents;

import java.util.Optional;

public interface LectureStudentsService {
    Optional<LecturesStudents> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);

    void createAndSaveLecturesStudents(Integer studentId, Integer lectureId, Integer focusTime);
}
