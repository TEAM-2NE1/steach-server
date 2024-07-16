package com.twentyone.steachserver.domain.lectureStudent.service;

import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;

import java.util.Optional;

public interface LectureStudentService {
    Optional<LectureStudent> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);

    void createAndSaveLectureStudent(Integer studentId, Integer lectureId, Integer focusTime);
}
