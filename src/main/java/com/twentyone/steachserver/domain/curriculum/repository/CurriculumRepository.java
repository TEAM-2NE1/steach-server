package com.twentyone.steachserver.domain.curriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {
    @Override
    Optional<Curriculum> findById(Integer id);
}
