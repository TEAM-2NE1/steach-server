package com.twentyone.steachserver.domain.studentLecture.service;

import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.member.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentLectureService {
    Optional<StudentLecture> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);
    List<StudentInfoByLectureDto> findAllStudentInfoByLectureId(Integer lectureId);
    void createAndSaveStudentLecture(Student student, Lecture lecture, Integer focusTime);

    void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime);


}
