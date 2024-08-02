package com.twentyone.steachserver.domain.quiz.dto;

import com.twentyone.steachserver.domain.quiz.model.Quiz;

import java.util.List;

public record QuizResponseDto(
        Integer quizId,
        Integer lectureId,
        Integer quizNumber,
        String question,
        List<String> choices,
        Integer answers
) {
    public static QuizResponseDto createQuizResponseDto(Integer lectureId, QuizRequestDto request, Integer quizId) {
        allStrip(request.choices());
//        allStrip(request.answers());
        return new QuizResponseDto(
                quizId,
                lectureId,
                request.quizNumber(),
                request.question(),
                request.choices(),
                request.answers()
        );
    }

    public static QuizResponseDto createQuizResponseDto(Quiz quiz, List<String> choices, Integer answers) {
        allStrip(choices);
//        allStrip(answers);
        return new QuizResponseDto(
                quiz.getId(),
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

    public static QuizResponseDto fromDomain(Quiz quiz) {
        return new QuizResponseDto(
                quiz.getId(),
                quiz.getLecture().getId(),
                quiz.getQuizNumber(),
                quiz.getQuestion(),
                quiz.getQuizChoiceString(),
                quiz.getAnswer()
        );
    }
}
