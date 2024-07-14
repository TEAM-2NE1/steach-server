package com.twentyone.steachserver.domain.studentsQuizzes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentQuizzesRepository extends JpaRepository<StudentsQuizzes, Integer> {
    Optional<StudentsQuizzes> findByQuizIdAndStudentId(Integer quizId, Integer studentId);
}
