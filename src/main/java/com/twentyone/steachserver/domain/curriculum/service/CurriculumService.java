package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;

import java.util.Optional;

public interface CurriculumService {
    Optional<Curriculum> findById(Integer id);

}
