package com.twentyone.steachserver.domain.lecture.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.*;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.model.QClassroom;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.QCurriculum;
import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.model.QLecture;
import com.twentyone.steachserver.domain.member.model.QStudent;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.twentyone.steachserver.domain.classroom.model.QClassroom.classroom;
import static com.twentyone.steachserver.domain.curriculum.model.QCurriculum.curriculum;
import static com.twentyone.steachserver.domain.lecture.model.QLecture.lecture;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
import static com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum.studentCurriculum;


public class LectureQueryRepository {
    private final JPAQueryFactory query;

    public LectureQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    public LectureBeforeStartingResponseDto findLectureDetailsByLectureId(Integer lectureId) {
        // 강의 정보 조회

        return query
                .select(Projections.constructor(
                        LectureBeforeStartingResponseDto.class,
                        lecture.lectureOrder,
                        curriculum.title,
                        lecture.title,
                        lecture.lectureStartDate
                ))
                .from(lecture)
                .join(lecture.curriculum, curriculum)
                .where(lecture.id.eq(lectureId))
                .fetchOne();
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
                // studentInfoLectureDto
                .map(ls -> new StudentInfoByLectureDto(
                        ls.getStudent().getStudentQuizzes().stream()
                                .map(sq -> new StudentQuizDto(sq.getScore(), sq.getStudentChoice(),sq.getStudent().getName()))
                                .collect(Collectors.toList()),
                        ls.getFocusRatio(),
                        ls.getFocusTime()
                ))
                .collect(Collectors.toList());

        return FinalLectureInfoByTeacherDto.createFinalLectureInfoByTeacherDto(studentInfoByLectureDtoList);
    }


    public Optional<Classroom> findClassroomByLectureAndStudent(Integer lectureId, Integer studentId) {
        QLecture qLecture = lecture;
        QCurriculum qCurriculum = curriculum;
        QStudentCurriculum qStudentCurriculum = studentCurriculum;
        QStudent qStudent = student;
        QClassroom qClassroom = classroom;

        // Lecture를 가져옴
        Lecture lecture = query.selectFrom(qLecture)
                .where(qLecture.id.eq(lectureId))
                .fetchOne();
        if (lecture == null) {
            throw new RuntimeException("Lecture not found");
        }

        // Curriculum을 가져옴
        Curriculum curriculum = lecture.getCurriculum();

        // StudentCurriculum 목록을 가져옴
        List<StudentCurriculum> studentCurricula = query.selectFrom(qStudentCurriculum)
                .where(qStudentCurriculum.curriculum.eq(curriculum))
                .fetch();

        for (StudentCurriculum studentCurriculum : studentCurricula) {
            Student student = studentCurriculum.getStudent();
            Integer id = student.getId();
            if (Objects.equals(id, studentId)) {
                Classroom classroom = query.selectFrom(qClassroom)
                        .where(qClassroom.lecture.id.eq(lectureId))
                        .fetchOne();
                return Optional.ofNullable(classroom);
            }
        }

        return Optional.empty();
    }

}
