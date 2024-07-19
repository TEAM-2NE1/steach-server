package com.twentyone.steachserver.domain.studentCurriculum.service;

import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;

import java.util.Optional;

public interface StudentCurriculumService {
    public Optional<StudentCurriculum> findById(Integer id);
}
