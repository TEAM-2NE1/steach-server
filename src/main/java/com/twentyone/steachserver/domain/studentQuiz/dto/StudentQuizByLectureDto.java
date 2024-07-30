package com.twentyone.steachserver.domain.studentQuiz.dto;

import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;

public record StudentQuizByLectureDto(Integer score, String student_choice) {
    public static StudentQuizByLectureDto createStudentQuizByLectureDto(StudentQuiz studentQuiz) {
        return new StudentQuizByLectureDto(studentQuiz.getScore(), studentQuiz.getStudentChoice());
    }
}
