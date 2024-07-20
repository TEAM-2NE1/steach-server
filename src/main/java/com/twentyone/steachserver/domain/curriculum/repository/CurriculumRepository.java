package com.twentyone.steachserver.domain.curriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Teacher;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Curriculum c join c.curriculumDetail where c.id = :curriculaId")
    Optional<Curriculum> findByIdWithLock(@Param("curriculaId") Integer curriculaId);

    @Query("select c from Curriculum c join c.curriculumDetail where c.id = :id")
    Optional<Curriculum> findByIdWithDetail(@Param("id") Integer id);

    @Query("select c from Curriculum  c join c.curriculumDetail where c.teacher = :teacher")
    Optional<List<Curriculum>> findAllByTeacher(@Param("teacher") Teacher teacher);

    Optional<Curriculum> findByLecturesContaining(Lecture lecture);
}
