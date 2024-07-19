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
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import com.twentyone.steachserver.domain.studentQuiz.model.QStudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.twentyone.steachserver.domain.studentLecture.model.QStudentLecture.studentLecture;
import static com.twentyone.steachserver.domain.member.model.QStudent.student;
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

    @Transactional
    public LectureBeforeStartingResponseDto getLectureBeforeStartingResponse(Integer lectureId) {

        QLecture qLecture = QLecture.lecture;
        QCurriculum qCurriculum = QCurriculum.curriculum;
        QCurriculumDetail qCurriculumDetail = QCurriculumDetail.curriculumDetail1;
        QStudentCurriculum qStudentCurriculum = QStudentCurriculum.studentCurriculum;
//        QStudent qStudent = QStudent.student;

        Optional<Lecture> lectureOptional = Optional.ofNullable(query
                .select(qLecture)
                .from(qLecture).join(qLecture.quizzes, QQuiz.quiz).fetchJoin()
                .where(qLecture.id.eq(lectureId))
                .fetchOne());
        if (lectureOptional.isEmpty()) {
            throw new IllegalStateException("lecture not found");
        }
        Lecture lecture = lectureOptional.get();

        Optional<Curriculum> curriculumOptional = Optional.ofNullable(query
                .selectFrom(qCurriculum)
                .where(qCurriculum.lectures.contains(lecture))
                .fetchOne());
        if (curriculumOptional.isEmpty()) {
            throw new IllegalStateException("curriculum not found");
        }
        Curriculum curriculum = curriculumOptional.get();
        SimpleCurriculumByLectureDto simpleCurriculumByLectureDto = SimpleCurriculumByLectureDto.createSimpleCurriculumByLectureDto(curriculum);

        Optional<CurriculumDetail> curriculumDetailOptional = Optional.ofNullable(query
                .selectFrom(qCurriculumDetail)
                .where(qCurriculumDetail.id.eq(curriculum.getId()))
                .fetchOne());
        if (curriculumDetailOptional.isEmpty()) {
            throw new IllegalStateException("curriculum detail not found");
        }
        CurriculumDetail curriculumDetail = curriculumDetailOptional.get();
        CurriculumDetailByLectureDto curriculumDetailByLectureDto = CurriculumDetailByLectureDto.createCurriculumDetailByLectureDto(curriculumDetail);

        List<StudentCurriculum> studentCurricula = query
                .selectFrom(qStudentCurriculum)
                .where(qStudentCurriculum.curriculum.eq(curriculum))
                .fetch();

        List<StudentByLectureDto> studentByLectureDtos = studentCurricula.stream()
                .map(sc -> StudentByLectureDto.createStudentByLectureDto(sc.getStudent()))
                .collect(Collectors.toList());

        return LectureBeforeStartingResponseDto.of(lecture, simpleCurriculumByLectureDto, curriculumDetailByLectureDto, studentByLectureDtos);
    }

    public List<StudentInfoByLectureDto> getStudentInfoByLecture(Integer lectureId) {
        QLecture qLecture = QLecture.lecture;
        QStudentLecture qStudentLecture = QStudentLecture.studentLecture;
        QStudentQuiz qStudentQuiz = QStudentQuiz.studentQuiz;
        QStudent qStudent = QStudent.student;

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
            List<StudentQuizDto> studentQuizDtos = studentQuizzes.stream().map(studentQuiz ->
                    StudentQuizDto.createStudentQuizDto(studentQuiz, studentQuiz.getStudent().getName())
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

        LocalDateTime realEndTime = query
                .select(lecture.realEndTime)
                .from(lecture)
                .where(lecture.id.eq(lectureId))
                .fetchOne();

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
                long calculatedFocusRatio = focusTime / lectureDurationMinutes;
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
