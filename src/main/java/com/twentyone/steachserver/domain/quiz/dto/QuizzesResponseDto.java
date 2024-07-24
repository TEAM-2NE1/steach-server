package com.twentyone.steachserver.domain.quiz.dto;

import java.util.List;

public record QuizzesResponseDto(List<QuizResponseDto> quizResponseDtos) {
    public static QuizzesResponseDto of(List<QuizResponseDto> quizResponseDtos) {
        return new QuizzesResponseDto(quizResponseDtos);
    }
}
