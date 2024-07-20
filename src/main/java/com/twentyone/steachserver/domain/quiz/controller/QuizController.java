package com.twentyone.steachserver.domain.quiz.controller;

import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "퀴즈 생성 ", description = "성공시 200 반환, 실패시 500 INTERNAL_SERVER_ERROR 반환")
    @PostMapping("/{lectureId}")
    public ResponseEntity<QuizResponseDto> createQuiz(@PathVariable("lectureId")Integer lectureId, @RequestBody QuizRequestDto request) throws Exception {
        Optional<Quiz> quiz = quizService.createQuiz(lectureId,request);
        if (quiz.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(QuizResponseDto.createQuizResponseDto(lectureId, request));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "퀴즈 조회 ", description = "성공시 200 반환, 실패시 204 NOT_FOUND 반환")
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponseDto> getQuizResponseDto(@PathVariable("quizId") Integer quizId) {
        Optional<QuizResponseDto> quizOptional = quizService.getQuizResponseDto(quizId);
        return quizOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
