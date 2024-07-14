package com.twentyone.steachserver.domain.quiz.repository;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
