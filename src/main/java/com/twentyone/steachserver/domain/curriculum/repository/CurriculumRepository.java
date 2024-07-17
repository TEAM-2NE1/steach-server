package com.twentyone.steachserver.domain.curriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

}
