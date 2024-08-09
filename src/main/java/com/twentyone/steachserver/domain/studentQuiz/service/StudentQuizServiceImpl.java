package com.twentyone.steachserver.domain.studentQuiz.service;

import com.twentyone.steachserver.domain.lecture.validator.LectureValidator;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizStatistics;
import com.twentyone.steachserver.domain.quiz.repository.QuizRepository;
import com.twentyone.steachserver.domain.quiz.repository.QuizStatisticsRepository;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizRequestDto;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuizId;
import com.twentyone.steachserver.domain.studentQuiz.repository.StudentQuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StudentQuizServiceImpl implements StudentQuizService {

    private final StudentQuizRepository studentQuizzesRepository;
    private final StudentRepository studentRepository;
    private final QuizRepository quizRepository;
    private final QuizStatisticsRepository quizStatisticsRepository;

    private final LectureValidator lectureValidator;

    @Secured("ROLE_STUDENT")
    @Transactional
    public StudentQuiz createStudentQuiz(Student student, Integer quizId, StudentQuizRequestDto requestDto){
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

        log.info("asdfdsf12222222222222");
//        lectureValidator.validateFinishLecture(quiz.getLecture());
        log.info("asdfdsf12222222222222");
//        lectureValidator.validateQuizOfLectureAuth(quiz, student);

        log.info("asdfdsf");
        StudentQuizId studentQuizId = StudentQuizId.createStudentQuizId(student.getId(), quizId);
        Optional<StudentQuiz> studentQuiz = studentQuizzesRepository.findById(studentQuizId);
        if (studentQuiz.isPresent()) throw new IllegalArgumentException("이미 점수가 존재합니다.");

        StudentQuiz newStudentQuiz = StudentQuiz.createStudentQuiz(student, quiz, requestDto);
        studentQuizzesRepository.save(newStudentQuiz);

        //통계생성 - 나중에 Redis로 바꾸지
        log.info("prev===========");
        log.info("lectureId: " + newStudentQuiz.getQuiz().getLecture().getId());
        log.info("stdId: " + newStudentQuiz.getStudent().getId());
        log.info("prev===========");
        QuizStatistics quizStatistics = quizStatisticsRepository.findByStudentIdAndLectureIdOrderByCurrentScoreDesc(student.getId(), quiz.getLecture().getId())
                .orElseGet(() -> new QuizStatistics(newStudentQuiz.getQuiz().getLecture().getId(), newStudentQuiz.getStudent().getId()));
        quizStatistics.update(requestDto.score());

        quizStatisticsRepository.save(quizStatistics);

        return newStudentQuiz;
    }
}
