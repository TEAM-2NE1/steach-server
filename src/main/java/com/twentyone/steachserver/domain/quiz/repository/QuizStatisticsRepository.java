package com.twentyone.steachserver.domain.quiz.repository;

import com.twentyone.steachserver.domain.quiz.model.QuizStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizStatisticsRepository extends JpaRepository<QuizStatistics, Integer> {
    Optional<QuizStatistics> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);
}
