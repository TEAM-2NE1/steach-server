package com.twentyone.steachserver.domain.studentLecture.repository;

import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLectureId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentLectureRepository extends JpaRepository<StudentLecture, StudentLectureId> {
    Optional<StudentLecture>  findByStudentIdAndLectureId(Integer studentId, Integer lectureId);
}
