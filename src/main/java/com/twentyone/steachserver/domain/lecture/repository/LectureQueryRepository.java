package com.twentyone.steachserver.domain.lecture.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.*;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.model.QClassroom;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailByLectureDto;
import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumByLectureDto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.model.QCurriculum;
import com.twentyone.steachserver.domain.curriculum.model.QCurriculumDetail;
import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.model.QLecture;
import com.twentyone.steachserver.domain.member.dto.StudentByLectureDto;
import com.twentyone.steachserver.domain.member.model.QStudent;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.quiz.model.QQuiz;
import com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizByLectureDto;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.twentyone.steachserver.domain.classroom.model.QClassroom.classroom;
import static com.twentyone.steachserver.domain.curriculum.model.QCurriculum.curriculum;
import static com.twentyone.steachserver.domain.curriculum.model.QCurriculumDetail.curriculumDetail;
import static com.twentyone.steachserver.domain.lecture.model.QLecture.lecture;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
import static com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum.studentCurriculum;
import static com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture.studentLecture;


public class LectureQueryRepository {
    private final JPAQueryFactory query;

    public LectureQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    @Transactional
    public LectureBeforeStartingResponseDto getLectureBeforeStartingResponse(Integer lectureId) {
        QLecture qLecture = lecture;
        QCurriculum qCurriculum = curriculum;
        QCurriculumDetail qCurriculumDetail = curriculumDetail;
        QStudentCurriculum qStudentCurriculum = studentCurriculum;
        QStudent qStudent = student;

        // 필요한 데이터를 한 번의 쿼리로 가져오기
        Tuple result = query
                .select(
                        qLecture,
                        qCurriculum,
                        qCurriculumDetail
                )
                .from(qLecture)
                .leftJoin(qLecture.curriculum, qCurriculum)
                .leftJoin(qCurriculum.curriculumDetail, qCurriculumDetail)
                .where(qLecture.id.eq(lectureId))
                .fetchOne();

        if (result == null) {
            throw new IllegalStateException("Lecture not found");
        }

        Lecture lecture = result.get(qLecture);
        Curriculum curriculum = result.get(qCurriculum);
        CurriculumDetail curriculumDetail = result.get(qCurriculumDetail);

        SimpleCurriculumByLectureDto simpleCurriculumByLectureDto = SimpleCurriculumByLectureDto.createSimpleCurriculumByLectureDto(Objects.requireNonNull(curriculum));
        CurriculumDetailByLectureDto curriculumDetailByLectureDto = CurriculumDetailByLectureDto.createCurriculumDetailByLectureDto(Objects.requireNonNull(curriculumDetail));

        List<StudentByLectureDto> studentByLectureDtos = query
                .select(Projections.constructor(
                        StudentByLectureDto.class,
                        qStudent.name,
                        qStudent.email
                ))
                .from(qStudent)
                .join(qStudent.studentCurricula, qStudentCurriculum)
                .where(qStudentCurriculum.curriculum.id.eq(curriculum.getId()))
                .fetch();

        return LectureBeforeStartingResponseDto.of(lecture, simpleCurriculumByLectureDto, curriculumDetailByLectureDto, studentByLectureDtos);
    }

    public Optional<Classroom> findClassroomByLectureAndStudent(Integer lectureId, Integer studentId) {
        QLecture qLecture = lecture;
        QStudentCurriculum qStudentCurriculum = studentCurriculum;
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
            Integer id = studentCurriculum.getStudent().getId();
            if (Objects.equals(id, studentId)) {
                Classroom classroom = query.selectFrom(qClassroom)
                        .where(qClassroom.lecture.id.eq(lectureId))
                        .fetchOne();
                return Optional.ofNullable(classroom);
            }
        }

        return Optional.empty();
    }

    public List<Student> getStudentIds(Integer lectureId) {
        return query.selectFrom(studentLecture)
                .where(studentLecture.lecture.id.eq(lectureId))
                .fetch()
                .stream()
                .map(StudentLecture::getStudent)
                .toList();
    }
}
