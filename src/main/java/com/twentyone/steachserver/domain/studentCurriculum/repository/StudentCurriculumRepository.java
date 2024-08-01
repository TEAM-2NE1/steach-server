package com.twentyone.steachserver.domain.studentCurriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculumId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentCurriculumRepository extends JpaRepository<StudentCurriculum, StudentCurriculumId> {
    @Query("select sc from StudentCurriculum sc join sc.curriculum where sc.student = :student")
    Page<StudentCurriculum> findByStudent(@Param("student") Student student, Pageable pageable);

    @Query("select sc from StudentCurriculum sc join sc.curriculum where sc.student = :student")
    List<StudentCurriculum> findByStudent(@Param("student") Student student);

    Optional<StudentCurriculum> findTop1ByStudentAndCurriculum(Student student, Curriculum curriculum);

    List<StudentCurriculum> findAllByCurriculumId(Integer curriculumId);

    void deleteByStudentAndCurriculum(Student student, Curriculum referenceById);
}
