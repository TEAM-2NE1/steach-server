package com.twentyone.steachserver.domain.studentsQuizzes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentQuizzesServiceImpl implements StudentQuizzesService {

    private final StudentQuizzesRepository studentQuizzesRepository;
    @Override
    public StudentsQuizzes findByQuizIdAndStudentId(Integer quizId, Integer studentId) {
        return studentQuizzesRepository.findByQuizIdAndStudentId(quizId, studentId)
                .orElseThrow(() -> new RuntimeException("StudentsQuizzes not found"));
    }
}
