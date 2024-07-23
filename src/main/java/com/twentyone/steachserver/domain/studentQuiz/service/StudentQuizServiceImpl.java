package com.twentyone.steachserver.domain.studentQuiz.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.validator.LectureValidator;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.repository.QuizRepository;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizRequestDto;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuizId;
import com.twentyone.steachserver.domain.studentQuiz.repository.StudentQuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentQuizServiceImpl implements StudentQuizService {

    private final StudentQuizRepository studentQuizzesRepository;
    private final StudentRepository studentRepository;
    private final QuizRepository quizRepository;

    private final LectureValidator lectureValidator;

    @Transactional
    public StudentQuiz createStudentQuiz(Integer studentId, Integer quizId, StudentQuizRequestDto requestDto){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

        lectureValidator.validateFinishLecture(quiz.getLecture());
        lectureValidator.validateQuizOfLectureAuth(quiz, student);

        Optional<StudentQuiz> studentQuiz = studentQuizzesRepository.findById(StudentQuizId.createStudentQuizId(studentId, quizId));
        if (studentQuiz.isPresent()) throw new IllegalArgumentException("이미 점수가 존재합니다.");

        StudentQuiz newStudentQuiz = StudentQuiz.createStudentQuiz(student, quiz, requestDto);
        studentQuizzesRepository.save(newStudentQuiz);
        return newStudentQuiz;
    }
}
