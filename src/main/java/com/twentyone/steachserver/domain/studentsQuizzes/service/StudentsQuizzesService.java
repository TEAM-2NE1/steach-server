package com.twentyone.steachserver.domain.studentsQuizzes.service;

import com.twentyone.steachserver.domain.studentsQuizzes.model.StudentsQuizzes;

public interface StudentsQuizzesService {
    StudentsQuizzes findByQuizIdAndStudentId(Integer quizId, Integer studentId);
    void enterScore(Integer studentId, Integer quizId, Integer score) throws IllegalAccessException;

}
