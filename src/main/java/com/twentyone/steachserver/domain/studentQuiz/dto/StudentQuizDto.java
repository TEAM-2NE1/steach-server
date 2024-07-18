package com.twentyone.steachserver.domain.studentQuiz.dto;

import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import lombok.Getter;

@Getter
public class StudentQuizDto {
    private final String studentName;
    private final Integer score;
    private final String student_choice;

    public StudentQuizDto(Integer score, String student_choice, String studentName) {
        this.studentName = studentName;
        this.score = score;
        this.student_choice = student_choice;
    }

    public static StudentQuizDto createStudentQuizDto(StudentQuiz studentQuiz, String studentName) {
        return new StudentQuizDto(studentQuiz.getScore(), studentQuiz.getStudentChoice(), studentName);
    }
}