package com.twentyone.steachserver.domain.quiz.dto;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record QuizResponseDto(
        Integer lectureId,
        Integer quizNumber,
        String question,
        List<String> choices,
        List<String> answers
) {
    public static QuizResponseDto createQuizResponseDto(Integer lectureId, QuizRequestDto request) {
        return new QuizResponseDto(
                lectureId,
                request.quizNumber(),
                request.question(),
                request.choices(),
                request.answers()
        );
    }

    public static QuizResponseDto createQuizResponseDto(Quiz quiz, List<String> choices, List<String> answers) {
        return new QuizResponseDto(
                quiz.getLecture().getId(),
                quiz.getQuizNumber(),
                quiz.getQuestion(),
                choices,
                answers
        );
    }
}
