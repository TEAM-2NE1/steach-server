package com.twentyone.steachserver.domain.lectureStudent.service;

import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;

import java.util.List;
import java.util.Optional;

public interface LectureStudentService {
    Optional<LectureStudent> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);
    List<StudentInfoByLectureDto> findAllStudentInfoByLectureId(Integer lectureId);
    void createAndSaveLectureStudent(Integer studentId, Integer lectureId, Integer focusTime);
}
