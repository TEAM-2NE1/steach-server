package com.twentyone.steachserver.domain.studentLecture.service;

public interface StudentLectureService {
    void saveSleepTime(Integer studentId, Integer lectureId, Integer sleepTime);

    void updateStudentLectureByFinishLecture(Integer lectureId);

//    void createStudentLectureByLecture(Integer lectureId);
}
