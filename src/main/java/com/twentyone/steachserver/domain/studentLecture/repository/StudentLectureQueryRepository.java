package com.twentyone.steachserver.domain.studentLecture.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture.studentLecture;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
import static com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz.studentQuiz;

public class StudentLectureQueryRepository {
    private final JPAQueryFactory query;

    public StudentLectureQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<StudentInfoByLectureDto> findAllStudentInfoByLectureId(Integer lectureId) {
        return query
                .select(Projections.constructor(
                        StudentInfoByLectureDto.class,
                        student.id,
                        student.name,
                        studentQuiz.score
                ))
                .from(studentLecture)
                .join(studentLecture.student, student)
                .join(studentQuiz).on(studentQuiz.student.id.eq(student.id))
                .where(studentLecture.lecture.id.eq(lectureId))
                .fetch();
    }
}
