package com.twentyone.steachserver.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QuizStudentScoreDto {
    private int rank;
    private int score;
    private String name;

    public void setCurrentRank(int rank) {
        this.rank = rank;
    }
}
