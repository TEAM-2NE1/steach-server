package com.twentyone.steachserver.domain.lecture.repository;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    List<Lecture> findByLectureStartDateBetween(LocalDateTime toMinute, LocalDateTime fromMinute);

    @Query(value = "select * from lectures where curriculum_id = :curriculumId", nativeQuery = true)
    Optional<List<Lecture>> findByCurriculumId(@Param("curriculumId")Integer curriculumId);

    @Query(value = "select l from Lecture l left join l.quizzes where l.id = :lectureId")
    Optional<Lecture> findByIdWithQuiz(@Param("lectureId")Integer lectureId);
}
