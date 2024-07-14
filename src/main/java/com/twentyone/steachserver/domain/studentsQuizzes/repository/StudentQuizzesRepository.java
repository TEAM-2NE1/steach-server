package com.twentyone.steachserver.domain.studentsQuizzes.repository;

import com.twentyone.steachserver.domain.studentsQuizzes.model.StudentsQuizzes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentQuizzesRepository extends JpaRepository<StudentsQuizzes, Integer> {
    Optional<StudentsQuizzes> findByQuizIdAndStudentId(Integer quizId, Integer studentId);
}
