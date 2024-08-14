package com.twentyone.steachserver.domain.studentLecture.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.model.QLecture;
import com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizByLectureDto;
import com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.twentyone.steachserver.domain.lecture.model.QLecture.lecture;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
import static com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture.studentLecture;
import static com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz.studentQuiz;

@Slf4j
@Repository
public class StudentLectureQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public StudentLectureQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public List<StudentLecture> findAllStudentInfoByLectureId(Integer lectureId) {
        return query
                .select(studentLecture)
                .from(studentLecture)
                .join(studentLecture.student, student)
                .join(studentQuiz).on(studentQuiz.student.id.eq(student.id))
                .where(studentLecture.lecture.id.eq(lectureId))
                .fetch();
    }

    //    public List<StudentInfoByLectureDto> getStudentInfoByLecture(Integer lectureId) {
//        QStudentLecture studentLecture = QStudentLecture.studentLecture;
//        QStudentQuiz studentQuiz = QStudentQuiz.studentQuiz;
//        QQuiz quiz = QQuiz.quiz;
//
//        // Fetch all quizzes for the lecture in a single query
//        List<Integer> quizIds = query.select(quiz.id)
//                .from(quiz)
//                .where(quiz.lecture.id.eq(lectureId))
//                .fetch();
//
//        // Fetch student lectures and their quizzes in a single optimized query
//        List<StudentLecture> studentLectures = query
//                .selectFrom(studentLecture)
//                .leftJoin(studentLecture.student).fetchJoin()
//                .leftJoin(studentLecture.student.studentQuizzes, studentQuiz).fetchJoin()
//                .where(studentLecture.lecture.id.eq(lectureId)
//                        .and(studentQuiz.quiz.id.in(quizIds))) // Ensure we only fetch relevant student quizzes
//                .fetch();
//
//        System.out.println(studentLectures);
//        // Transform the fetched data into the required DTOs
//        return studentLectures.stream()
//                .map(sl -> StudentInfoByLectureDto.of(
//                        sl.getStudent().getStudentQuizzes().stream()
//                                .filter(sq -> quizIds.contains(sq.getQuiz().getId())) // Filter quizzes again to be safe
//                                .map(StudentQuizByLectureDto::createStudentQuizByLectureDto)
//                                .collect(Collectors.toList()),
//                        sl
//                ))
//                .toList();
//    }
    public List<StudentInfoByLectureDto> getStudentInfoByLecture(Integer lectureId) {
        QLecture qLecture = lecture;
        QStudentLecture qStudentLecture = QStudentLecture.studentLecture;
        QStudentQuiz qStudentQuiz = QStudentQuiz.studentQuiz;

        // Lecture를 가져옴
        Lecture lecture = query.selectFrom(qLecture)
                .where(qLecture.id.eq(lectureId))
                .fetchOne();
        if (lecture == null) {
            throw new IllegalStateException("lecture not found");
        }

        // StudentLecture 목록을 가져옴
        List<StudentLecture> studentLectures = query.selectFrom(qStudentLecture)
                .where(qStudentLecture.lecture.eq(lecture))
                .fetch();

        // StudentInfoByLectureDto 리스트를 생성
        return studentLectures.stream().map(studentLecture -> {
            // 각 StudentLecture에 대한 StudentQuiz 목록을 가져옴
            List<StudentQuiz> studentQuizzes = query.selectFrom(qStudentQuiz)
                    .where(qStudentQuiz.student.eq(studentLecture.getStudent()))
                    .fetch();

            // StudentQuizDto 리스트를 생성
            List<StudentQuizByLectureDto> studentQuizDtos = studentQuizzes.stream().map(StudentQuizByLectureDto::createStudentQuizByLectureDto
            ).collect(Collectors.toList());

            // StudentInfoByLectureDto 생성
            return StudentInfoByLectureDto.of(studentQuizDtos, studentLecture);
        }).collect(Collectors.toList());
    }


    @Transactional
    public void updateStudentLectureByFinishLecture(Integer lectureId) {
        QStudentLecture studentLecture = QStudentLecture.studentLecture;
        QLecture lecture = QLecture.lecture;
        QStudentQuiz studentQuiz = QStudentQuiz.studentQuiz;

        // 강의의 시작 시간과 종료 시간을 가져옵니다.
        LocalDateTime realStartTime = query
                .select(lecture.realStartTime)
                .from(lecture)
                .where(lecture.id.eq(lectureId))
                .fetchOne();


        if (realStartTime == null) {
            throw new IllegalStateException("시작하지 않은 강의입니다. (realStartTime is null)");
        }

        LocalDateTime realEndTime = query
                .select(lecture.realEndTime)
                .from(lecture)
                .where(lecture.id.eq(lectureId))
                .fetchOne();

        if (realEndTime == null) {
            throw new IllegalStateException("시작하지 않은 강의입니다. (realEndTime is null)");
        }

        long lectureDurationMinutes = Duration.between(realStartTime, realEndTime).toMinutes();

        // 강의의 모든 퀴즈를 통해 각 학생별 StudentQuiz를 가져옵니다.
        List<StudentQuiz> studentQuizzes = query
                .selectFrom(studentQuiz)
                .where(studentQuiz.quiz.lecture.id.eq(lectureId))
                .fetch();

        // 학생별 StudentQuiz를 그룹화합니다.
        Map<Integer, List<StudentQuiz>> studentQuizMap = studentQuizzes.stream()
                .collect(Collectors.groupingBy(sq -> sq.getStudent().getId()));

        List<StudentLecture> studentLectures = query
                .selectFrom(studentLecture)
                .join(studentLecture.lecture, lecture)
                .where(lecture.id.eq(lectureId))
                .fetch();

        for (StudentLecture sl : studentLectures) {
            Integer focusTime = sl.getFocusTime();
            //lectureDurationMinutes = 60; //TODO test용, 삭제 꼭 하기

            // focusRatio 계산 (졸음 비율)
            if (focusTime != null && lectureDurationMinutes > 0) {
                BigDecimal focusTimeBD = BigDecimal.valueOf(focusTime);
                BigDecimal lectureDurationBD = BigDecimal.valueOf(lectureDurationMinutes);

                BigDecimal calculatedFocusRatio = focusTimeBD
                        .divide(lectureDurationBD, 5, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);

                sl.updateFocusRatio(calculatedFocusRatio);
            }
//            System.out.println(sl);

            // 학생별 퀴즈 총점수 및 정답 맞춘 수 계산
            List<StudentQuiz> quizzes = studentQuizMap.get(sl.getStudent().getId());
            if (quizzes != null) {
                int totalScore = quizzes.stream().mapToInt(StudentQuiz::getScore).sum();
                int correctAnswers = (int) quizzes.stream().filter(sq -> sq.getScore() > 0).count();

                sl.updateQuizTotalScore(totalScore);
                sl.updateQuizAnswerCount(correctAnswers);
            }

            // EntityManager를 이용하여 업데이트 수행
            em.merge(sl);
        }
    }

}
