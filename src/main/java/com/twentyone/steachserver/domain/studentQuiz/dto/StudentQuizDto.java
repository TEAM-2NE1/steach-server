package com.twentyone.steachserver.domain.studentQuiz.dto;

import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;

public record StudentQuizDto(Integer score, String student_choice, String studentName) {
    public static StudentQuizDto createStudentQuizDto(StudentQuiz studentQuiz, String studentName) {
        return new StudentQuizDto(studentQuiz.getScore(), studentQuiz.getStudentChoice(), studentName);
    }

}
