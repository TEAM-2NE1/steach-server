package com.twentyone.steachserver.domain.studentLecture.service;

import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;

import java.util.List;
import java.util.Optional;

public interface StudentLectureService {
    void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime);

    void updateStudentLectureByFinishLecture(Integer lectureId);

    void createStudentLectureByLecture(Integer lectureId);
}
