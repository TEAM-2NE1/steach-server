package com.twentyone.steachserver.domain.studentQuiz.service;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.service.QuizService;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizRequestDto;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.repository.StudentQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentQuizServiceImpl implements StudentQuizService {

    private final StudentQuizRepository studentQuizzesRepository;

    private final StudentService studentService;
    private final QuizService quizService;

    @Override
    public StudentQuiz findByQuizIdAndStudentId(Integer quizId, Integer studentId) {
        return studentQuizzesRepository.findByQuizIdAndStudentId(quizId, studentId)
                .orElseThrow(() -> new RuntimeException("StudentQuiz not found"));
    }

    @Override
    public void createStudentQuiz(Integer studentId, Integer quizId, StudentQuizRequestDto requestDto) throws IllegalAccessException {
        StudentQuiz studentQuiz = findByQuizIdAndStudentId(quizId, studentId);
        if (studentQuiz != null) {
            throw new IllegalAccessException("StudentQuiz already exists");
        }

        Optional<Student> studentById = studentService.findStudentById(studentId);
        Optional<Quiz> quiz = quizService.findQuizById(quizId);

        if (studentById.isPresent() && quiz.isPresent()) {
            StudentQuiz newStudentQuiz = StudentQuiz.createStudentQuiz(studentById.get(), quiz.get(), requestDto);
            studentQuizzesRepository.save(newStudentQuiz);
        }
        else {
            throw new RuntimeException("Student or Quiz not found");
        }
    }

}
