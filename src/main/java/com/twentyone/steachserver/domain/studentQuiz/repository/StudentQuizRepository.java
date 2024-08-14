package com.twentyone.steachserver.domain.studentQuiz.repository;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuizId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentQuizRepository extends JpaRepository<StudentQuiz, StudentQuizId> {
    List<StudentQuiz> findTop4StudentQuizByQuizOrderByScoreDesc(Quiz quiz);

    Optional<StudentQuiz> findByStudentAndQuiz(Student student, Quiz quiz);
}
