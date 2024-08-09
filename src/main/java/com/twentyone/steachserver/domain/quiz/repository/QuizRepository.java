package com.twentyone.steachserver.domain.quiz.repository;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findALlByLectureId(Integer lectureId);

    @Query("select q from Quiz q join q.quizChoices where q.id = :quizId")
    Optional<Quiz> findByIdWithQuizChoice(@Param("quizId") Integer quizId);
}
