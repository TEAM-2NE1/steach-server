package com.twentyone.steachserver.domain.studentQuiz.dto;

import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import lombok.Getter;

@Getter
public class StudentQuizRequestDto {
    private final String studentName;
    private final Integer score;
    private final String studentChoice;

    public StudentQuizRequestDto(Integer score, String studentChoice, String studentName) {
        this.score = score;
        this.studentChoice = studentChoice;
        this.studentName = studentName;
    }

}
