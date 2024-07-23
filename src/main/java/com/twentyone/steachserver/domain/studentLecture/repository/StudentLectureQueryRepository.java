package com.twentyone.steachserver.domain.studentLecture.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailByLectureDto;
import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumByLectureDto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.model.QCurriculum;
import com.twentyone.steachserver.domain.curriculum.model.QCurriculumDetail;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.model.QLecture;
import com.twentyone.steachserver.domain.member.dto.StudentByLectureDto;
import com.twentyone.steachserver.domain.member.model.QStudent;
import com.twentyone.steachserver.domain.quiz.model.QQuiz;
import com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizByLectureDto;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.twentyone.steachserver.domain.curriculum.model.QCurriculum.curriculum;
import static com.twentyone.steachserver.domain.curriculum.model.QCurriculumDetail.curriculumDetail;
import static com.twentyone.steachserver.domain.lecture.model.QLecture.lecture;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
import static com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum.studentCurriculum;
import static com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture.studentLecture;
import static com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz.studentQuiz;

public class StudentLectureQueryRepository {
    private final JPAQueryFactory query;
    private final EntityManager em;

    public StudentLectureQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
        this.em = em;
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

    public List<StudentInfoByLectureDto> getStudentInfoByLecture(Integer lectureId) {
        QStudentLecture studentLecture = QStudentLecture.studentLecture;
        QStudentQuiz studentQuiz = QStudentQuiz.studentQuiz;
        QQuiz quiz = QQuiz.quiz;

        List<Integer> quizIds = query.select(quiz.id)
                .from(quiz)
                .where(quiz.lecture.id.eq(lectureId))
                .stream().toList();

        // 아래의 studentQuizzes에서 quiz_id가 quizIds와 같은것들만 가져와줘
        return query
                .selectFrom(studentLecture)
                .leftJoin(studentLecture.student).fetchJoin()
                .leftJoin(studentLecture.student.studentQuizzes, studentQuiz).fetchJoin()
                .where(studentLecture.lecture.id.eq(lectureId)
                        .and(studentQuiz.quiz.id.in(quizIds))) // quizIds와 일치하는 studentQuiz만 조회
                .fetch()
                .stream()
                .map(sl -> StudentInfoByLectureDto.of(
                        sl.getStudent().getStudentQuizzes().stream()
                                .filter(sq -> quizIds.contains(sq.getQuiz().getId())) // quizIds 필터링 (한번 더 확인해주는 코드)
                                .map(StudentQuizByLectureDto::createStudentQuizByLectureDto)
                                .collect(Collectors.toList()),
                        sl
                ))
                .toList();
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


        if(realStartTime == null) {
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

            // focusRatio 계산 (졸음 비율)
            if (focusTime != null && lectureDurationMinutes > 0) {
                long calculatedFocusRatio = focusTime / lectureDurationMinutes * 100;
                sl.updateFocusRatio(calculatedFocusRatio);
            }

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
