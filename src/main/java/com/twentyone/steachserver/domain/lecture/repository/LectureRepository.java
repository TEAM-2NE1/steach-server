package com.twentyone.steachserver.domain.lecture.repository;

import com.twentyone.steachserver.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
}
