package com.twentyone.steachserver.domain.quiz.controller;

import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private QuizService quizService;
    private LectureService lectureService;

    @PostMapping("/{lectureId}")
    public ResponseEntity<QuizResponseDto> createQuiz(@PathVariable Integer lectureId, @RequestBody QuizRequestDto request) throws Exception {
        Optional<Quiz> quiz = quizService.createQuiz(lectureId,request);
        if (quiz.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(QuizResponseDto.createQuizResponseDto(lectureId, request));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponseDto> getQuizResponseDto(@PathVariable Integer quizId) {
        Optional<QuizResponseDto> quizOptional = quizService.getQuizResponseDto(quizId);
        return quizOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
