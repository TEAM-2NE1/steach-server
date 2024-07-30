package com.twentyone.steachserver.domain.studentQuiz.repository;

import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuizId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentQuizRepository extends JpaRepository<StudentQuiz, StudentQuizId> {
}
