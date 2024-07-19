package com.twentyone.steachserver.domain.studentCurriculum.repository;

import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCurriculumRepository extends JpaRepository<StudentCurriculum, Integer> {
}
