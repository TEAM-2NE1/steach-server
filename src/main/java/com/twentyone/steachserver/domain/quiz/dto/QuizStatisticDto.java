package com.twentyone.steachserver.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QuizStatisticDto {
    private List<Integer> statistics;
    private List<QuizStudentScoreDto> prev;
    private List<QuizStudentScoreDto> current;
}
