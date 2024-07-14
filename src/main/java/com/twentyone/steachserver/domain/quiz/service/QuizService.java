package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;

public interface QuizService {
    // Quiz methods
    QuizResponseDto createQuiz(QuizRequestDto request) throws Exception;

    void enterScore(Integer studentId, Integer quizId, Integer score);
}

