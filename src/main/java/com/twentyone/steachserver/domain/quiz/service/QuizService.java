package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;

import java.util.Optional;


public interface QuizService {
    // Quiz methods
    Optional<QuizResponseDto> createQuiz(QuizRequestDto request) throws Exception;

    Optional<QuizResponseDto> getQuizResponseDto(Integer quizId);

    Optional<Quiz> findQuizById(Integer quizId);
}

