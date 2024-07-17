package com.twentyone.steachserver.domain.curriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculumDetailRepository extends JpaRepository<CurriculumDetail, Integer> {
    //Optional<CurriculumDetail> findByCurriculumId(Integer curriculumId);
}
