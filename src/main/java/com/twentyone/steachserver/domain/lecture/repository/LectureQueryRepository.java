package com.twentyone.steachserver.domain.lecture.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.*;
import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.QLecture;
import com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static com.twentyone.steachserver.domain.curriculum.model.QCurriculum.curriculum;
import static com.twentyone.steachserver.domain.lecture.model.QLecture.lecture;


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
//                .from(studentLecture)
//                .join(studentLecture.student, student)
//                .join(studentQuiz).on(studentQuiz.student.id.eq(student.id))
//                .where(studentLecture.lecture.id.eq(lectureId))
//                .fetch();
//
//        // LectureResponseDto에 학생 정보 추가
//        if (lectureDetails != null) {
//            lectureDetails.getQuizzes().addAll(studentInfoList);
//        }

        return lectureDetails;
    }

    public FinalLectureInfoByTeacherDto getFinalLectureInfoByTeacher(Integer lectureId) {
        QLecture lecture = QLecture.lecture;
        QStudentLecture studentLecture = QStudentLecture.studentLecture;
        QStudentQuiz studentQuiz = QStudentQuiz.studentQuiz;

        List<StudentInfoByLectureDto> studentInfoByLectureDtoList = query
                .selectFrom(studentLecture)
                .leftJoin(studentLecture.student).fetchJoin()
                .leftJoin(studentLecture.student.studentQuizzes, studentQuiz).fetchJoin()
                .where(studentLecture.lecture.id.eq(lectureId))
                .fetch()
                .stream()
                .map(ls -> new StudentInfoByLectureDto(
                        ls.getStudent().getStudentQuizzes().stream()
                                .map(sq -> new StudentQuizDto(sq.getScore(), sq.getStudentChoice(),sq.getStudent().getName()))
                                .collect(Collectors.toList()),
                        ls.getFocusRatio().intValue(),
                        ls.getFocusTime()
                ))
                .collect(Collectors.toList());

        return FinalLectureInfoByTeacherDto.createFinalLectureInfoByTeacherDto(studentInfoByLectureDtoList);
    }

}
