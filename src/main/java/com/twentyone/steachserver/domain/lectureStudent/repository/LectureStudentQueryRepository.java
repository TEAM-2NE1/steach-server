package com.twentyone.steachserver.domain.lectureStudent.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.twentyone.steachserver.domain.lectureStudent.model.QLectureStudent.lectureStudent;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
import static com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz.studentQuiz;

public class LectureStudentQueryRepository {
    private final JPAQueryFactory query;

    public LectureStudentQueryRepository(EntityManager em) {
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
                .from(lectureStudent)
                .join(lectureStudent.student, student)
                .join(studentQuiz).on(studentQuiz.student.id.eq(student.id))
                .where(lectureStudent.lecture.id.eq(lectureId))
                .fetch();
    }
}
