package com.twentyone.steachserver.domain.studentsQuizzes.service;

import com.twentyone.steachserver.domain.studentsQuizzes.model.StudentsQuizzes;
import com.twentyone.steachserver.domain.studentsQuizzes.repository.StudentQuizzesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentQuizzesServiceImpl implements StudentQuizzesService {

    private final StudentQuizzesRepository studentQuizzesRepository;

    @Override
    public StudentsQuizzes findByQuizIdAndStudentId(Integer quizId, Integer studentId) {
        return studentQuizzesRepository.findByQuizIdAndStudentId(quizId, studentId)
                .orElseThrow(() -> new RuntimeException("StudentsQuizzes not found"));
    }

    @Override
    public void enterScore(Integer studentId, Integer quizId, Integer score) {
        StudentsQuizzes byQuizIdAndStudentId = findByQuizIdAndStudentId(quizId, studentId);
        byQuizIdAndStudentId.updateScore(score);
    }
}
