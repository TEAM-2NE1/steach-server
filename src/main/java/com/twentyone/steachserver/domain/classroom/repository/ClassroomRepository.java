package com.twentyone.steachserver.domain.classroom.repository;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
}
