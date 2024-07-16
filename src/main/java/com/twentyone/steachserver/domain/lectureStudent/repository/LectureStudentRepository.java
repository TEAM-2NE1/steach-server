package com.twentyone.steachserver.domain.lectureStudent.repository;

import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;
import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureStudentRepository extends JpaRepository<LectureStudent, LectureStudentId> {
    Optional<LectureStudent>  findByStudentIdAndLectureId(Integer studentId, Integer lectureId);
}
