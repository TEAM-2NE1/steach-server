package com.twentyone.steachserver.domain.studentCurriculum.repository;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculumId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentCurriculumRepository extends JpaRepository<StudentCurriculum, StudentCurriculumId> {
    @Query("select sc from StudentCurriculum sc join sc.curriculum where sc.student = :student")
    Optional<List<StudentCurriculum>> findByStudent(@Param("student") Student student);
}
