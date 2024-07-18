package com.twentyone.steachserver.domain.lecture.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.*;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.twentyone.steachserver.domain.curriculum.model.QCurriculum.curriculum;
import static com.twentyone.steachserver.domain.lecture.model.QLecture.lecture;
import static com.twentyone.steachserver.domain.lectureStudent.model.QLectureStudent.lectureStudent;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
import static com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz.studentQuiz;

public class LectureQueryRepository {
    private final JPAQueryFactory query;

    public LectureQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    public LectureBeforeStartingResponseDto findLectureDetailsByLectureId(Integer lectureId) {
        // 강의 정보 조회
        LectureBeforeStartingResponseDto lectureDetails = query
                .select(Projections.constructor(
                        LectureBeforeStartingResponseDto.class,
                        lecture.lectureOrder,
                        curriculum.title,
                        lecture.title,
                        lecture.lectureStartTime
                ))
                .from(lecture)
                .join(lecture.curriculum, curriculum)
                .where(lecture.id.eq(lectureId))
                .fetchOne();

        // 학생 정보 조회
//        List<StudentInfoByLectureDto> studentInfoList = query
//                .select(Projections.constructor(
//                        StudentInfoByLectureDto.class,
//                        student.id,
//                        student.name,
//                        studentQuiz.totalScore
//                ))
//                .from(lectureStudent)
//                .join(lectureStudent.student, student)
//                .join(studentQuiz).on(studentQuiz.student.id.eq(student.id))
//                .where(lectureStudent.lecture.id.eq(lectureId))
//                .fetch();
//
//        // LectureResponseDto에 학생 정보 추가
//        if (lectureDetails != null) {
//            lectureDetails.getQuizzes().addAll(studentInfoList);
//        }

        return lectureDetails;
    }
}
