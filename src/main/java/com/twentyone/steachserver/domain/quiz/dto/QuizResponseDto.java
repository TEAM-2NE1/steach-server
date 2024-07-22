package com.twentyone.steachserver.domain.quiz.dto;

import com.twentyone.steachserver.domain.quiz.model.Quiz;

import java.util.List;

public record QuizResponseDto(
        Integer lectureId,
        Integer quizNumber,
        String question,
        List<String> choices,
        List<String> answers
) {
    public static QuizResponseDto createQuizResponseDto(Integer lectureId, QuizRequestDto request) {
        allStrip(request.choices());
        allStrip(request.answers());
        return new QuizResponseDto(
                lectureId,
                request.quizNumber(),
                request.question(),
                request.choices(),
                request.answers()
        );
    }

    public static QuizResponseDto createQuizResponseDto(Quiz quiz, List<String> choices, List<String> answers) {
        allStrip(choices);
        allStrip(answers);
        return new QuizResponseDto(
                quiz.getLecture().getId(),
                quiz.getQuizNumber(),
                quiz.getQuestion(),
                choices,
                answers
        );
    }

    private static void allStrip(List<String> strings){
        strings.replaceAll(String::strip);
    }
}
