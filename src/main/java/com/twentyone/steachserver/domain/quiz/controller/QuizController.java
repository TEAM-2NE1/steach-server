package com.twentyone.steachserver.domain.quiz.controller;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizzesResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.service.QuizService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "퀴즈")
@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @Operation(summary = "퀴즈 생성!", description = "성공시 200 반환, 실패시 500 INTERNAL_SERVER_ERROR 반환")
    @PostMapping("/{lectureId}")
    public ResponseEntity<QuizResponseDto> createQuiz(@PathVariable("lectureId")Integer lectureId, @RequestBody QuizRequestDto request) throws Exception {
        return quizService.createQuiz(lectureId, request)
                .map(quiz -> ResponseEntity.status(HttpStatus.CREATED).body(QuizResponseDto.createQuizResponseDto(lectureId, request)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Operation(summary = "퀴즈 조회!", description = "성공시 200 반환, 실패시 204 NOT_FOUND 반환")
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponseDto> getQuizResponseDto(@PathVariable("quizId") Integer quizId) {
        return quizService.findById(quizId)
                .map(quiz -> ResponseEntity.ok().body(quizService.mapToDto(quiz)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "강의에 대한 퀴즈 조회!", description = "무조건 200 반환")
    @GetMapping("/lecture/{lectureId}/")
    public ResponseEntity<QuizzesResponseDto> getQuizzesResponseDto(@PathVariable("lectureId")Integer lectureId) {
        List<Quiz> quizzes = quizService.findAllByLectureId(lectureId);

        // Use Stream API to map entities to DTOs
        List<QuizResponseDto> quizResponseDtos = quizzes.stream()
                .map(quizService::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(QuizzesResponseDto.of(quizResponseDtos));
    }
}
