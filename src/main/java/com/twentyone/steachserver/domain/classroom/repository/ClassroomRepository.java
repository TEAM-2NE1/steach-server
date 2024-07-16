package com.twentyone.steachserver.domain.classroom.repository;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByLectureId(Integer lecture_id);
}
