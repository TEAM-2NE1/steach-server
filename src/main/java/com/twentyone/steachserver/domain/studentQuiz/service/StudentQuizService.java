package com.twentyone.steachserver.domain.studentQuiz.service;

import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;

public interface StudentQuizService {
    StudentQuiz findByQuizIdAndStudentId(Integer quizId, Integer studentId);
    void enterScore(Integer studentId, Integer quizId, Integer score) throws IllegalAccessException;

}
