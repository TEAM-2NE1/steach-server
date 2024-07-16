package com.twentyone.steachserver.domain.lectureStudents.repository;

import com.twentyone.steachserver.domain.lectureStudents.model.LecturesStudents;
import com.twentyone.steachserver.domain.lectureStudents.model.LecturesStudentsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturesStudentsRepository extends JpaRepository<LecturesStudents, LecturesStudentsId> {
    Optional<LecturesStudents>  findByStudentIdAndLectureId(Integer studentId, Integer lectureId);
}
