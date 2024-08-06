package com.twentyone.steachserver.domain.studentLecture.service;

public interface StudentLectureService {
    void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime);

    void updateStudentLectureByFinishLecture(Integer lectureId);

//    void createStudentLectureByLecture(Integer lectureId);
}
