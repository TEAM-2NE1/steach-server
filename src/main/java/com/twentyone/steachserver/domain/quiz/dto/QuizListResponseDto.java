package com.twentyone.steachserver.domain.quiz.dto;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record QuizListResponseDto(List<QuizResponseDto> quizList) {
    public static QuizListResponseDto fromDomainList(List<Quiz> quizList) {
        List<QuizResponseDto> list = new ArrayList<>();

        for (Quiz quiz : quizList) {
            list.add(QuizResponseDto.fromDomain(quiz));
        }

        return new QuizListResponseDto(list);
    }
}