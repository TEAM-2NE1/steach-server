package com.twentyone.steachserver.domain.studentQuiz.controller;

import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizRequestDto;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentQuiz.service.StudentQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studentsQuizzes")
@RequiredArgsConstructor
public class StudentQuizController {

    private final StudentQuizService studentQuizService;

    @PostMapping("/{studentId}/{quizId}")
    public ResponseEntity<?> createStudentQuiz(@PathVariable("studentId") Integer studentId, @PathVariable("quizId") Integer quizId, @RequestBody StudentQuizRequestDto requestDto) throws Exception {
        StudentQuiz studentQuiz = studentQuizService.createStudentQuiz(studentId, quizId, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK).body(studentQuiz);
    }

}
