package com.twentyone.steachserver.domain.studentsQuizzes;

public interface StudentQuizzesService {
    StudentsQuizzes findByQuizIdAndStudentId(Integer quizId, Integer studentId);
}
