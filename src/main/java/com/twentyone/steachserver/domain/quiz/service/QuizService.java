package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;

import java.util.Optional;

public interface QuizService {
    // Quiz methods
    Optional<QuizResponseDto> createQuiz(QuizRequestDto request) throws Exception;

    void enterScore(Integer studentId, Integer quizId, Integer score);

    Optional<QuizResponseDto> getQuizResponseDto(Integer quizId);
}

