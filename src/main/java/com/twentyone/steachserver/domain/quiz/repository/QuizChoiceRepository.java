package com.twentyone.steachserver.domain.quiz.repository;

import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizChoiceRepository extends JpaRepository<QuizChoice, Integer> {

}
