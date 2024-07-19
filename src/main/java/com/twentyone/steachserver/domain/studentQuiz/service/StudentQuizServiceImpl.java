package com.twentyone.steachserver.domain.studentQuiz.service;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.repository.QuizRepository;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizRequestDto;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.repository.StudentQuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentQuizServiceImpl implements StudentQuizService {

    private final StudentQuizRepository studentQuizzesRepository;
    private final StudentRepository studentRepository;
    private final QuizRepository quizRepository;

    @Override
    public StudentQuiz findByQuizIdAndStudentId(Integer quizId, Integer studentId) {
        return studentQuizzesRepository.findByQuizIdAndStudentId(quizId, studentId)
                .orElseThrow(() -> new RuntimeException("StudentQuiz not found"));
    }

    @Transactional
    public StudentQuiz createStudentQuiz(Integer studentId, Integer quizId, StudentQuizRequestDto requestDto){
        Student student = studentRepository.getReferenceById(studentId);
        Quiz quiz = quizRepository.getReferenceById(quizId);

        StudentQuiz newStudentQuiz = StudentQuiz.createStudentQuiz(student, quiz, requestDto);
        studentQuizzesRepository.save(newStudentQuiz);
        return newStudentQuiz;
    }
}
