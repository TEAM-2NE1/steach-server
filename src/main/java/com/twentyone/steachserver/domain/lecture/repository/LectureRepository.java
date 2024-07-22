package com.twentyone.steachserver.domain.lecture.repository;


import com.twentyone.steachserver.domain.lecture.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    List<Lecture> findByLectureStartDateBetween(LocalDateTime toMinute, LocalDateTime fromMinute);
}
