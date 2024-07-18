package com.twentyone.steachserver.domain.curriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Curriculum c where c.id = :curriculaId")
    Optional<Curriculum> findByIdWithLock(Integer curriculaId);
}
