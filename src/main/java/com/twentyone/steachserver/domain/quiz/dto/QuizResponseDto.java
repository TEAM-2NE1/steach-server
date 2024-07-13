package com.twentyone.steachserver.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter(value = AccessLevel.PRIVATE)
@Setter(value = AccessLevel.PRIVATE)
public class QuizResponseDto {
    private Integer lectureId;
    private Integer quizNumber;
    private String question;
    private List<String> choices;
    private List<String> answers;

    public static QuizResponseDto createQuizResponseDto(QuizRequestDto request) {
        QuizResponseDto responseDto = new QuizResponseDto();
        responseDto.setLectureId(request.getLectureId());
        responseDto.setQuizNumber(request.getQuizNumber());
        responseDto.setQuestion(request.getQuestion());
        responseDto.setChoices(request.getChoices());
        responseDto.setAnswers(request.getAnswers());
        return responseDto;
    }
}
