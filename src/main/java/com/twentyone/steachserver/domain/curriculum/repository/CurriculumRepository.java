package com.twentyone.steachserver.domain.curriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.member.model.Teacher;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Curriculum c where c.id = :curriculaId")
    Optional<Curriculum> findByIdWithLock(Integer curriculaId);

    @Query("select c from Curriculum  c join c.curriculumDetail where c.teacher = :teacher")
    Optional<List<Curriculum>> findAllByTeacher(Teacher teacher);

    @Override
    Optional<Curriculum> findById(Integer id);
}
