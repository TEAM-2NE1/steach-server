package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;

import java.util.List;
import java.util.Optional;


public interface QuizService {
    // Quiz methods
    Optional<Quiz> createQuiz(Integer lectureId, QuizRequestDto request) throws Exception;

    Optional<Quiz> findById(Integer quizId);

    QuizResponseDto mapToDto(Quiz quiz);

    List<Quiz> findAllByLectureId(Integer lectureId);
}

