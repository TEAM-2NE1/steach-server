package com.twentyone.steachserver.domain.studentsQuizzes.service;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.service.QuizService;
import com.twentyone.steachserver.domain.studentsQuizzes.model.StudentsQuizzes;
import com.twentyone.steachserver.domain.studentsQuizzes.repository.StudentQuizzesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentsQuizzesServiceImpl implements StudentsQuizzesService {

    private final StudentQuizzesRepository studentQuizzesRepository;

    private final StudentService studentService;
    private final QuizService quizService;

    @Override
    public StudentsQuizzes findByQuizIdAndStudentId(Integer quizId, Integer studentId) {
        return studentQuizzesRepository.findByQuizIdAndStudentId(quizId, studentId)
                .orElseThrow(() -> new RuntimeException("StudentsQuizzes not found"));
    }

    @Override
    public void enterScore(Integer studentId, Integer quizId, Integer score) throws IllegalAccessException {
        StudentsQuizzes studentsQuizzes = findByQuizIdAndStudentId(quizId, studentId);
        if (studentsQuizzes != null) {
            throw new IllegalAccessException("StudentsQuizzes already exists");
        }

        Optional<Student> studentById = studentService.findStudentById(studentId);
        Optional<Quiz> quiz = quizService.findQuizById(quizId);

        if (studentById.isPresent() && quiz.isPresent()) {
            StudentsQuizzes newStudentsQuizzes = StudentsQuizzes.createStudentsQuizzes(studentById.get(), quiz.get(), score);
            newStudentsQuizzes.updateScore(score);
            studentQuizzesRepository.save(newStudentsQuizzes);
        }
        else {
            throw new RuntimeException("Student or Quiz not found");
        }
    }
}
