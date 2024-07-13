package com.twentyone.steachserver.domain.quiz;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {

    private QuizService quizService;

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizRequestDto request) {
        QuizResponseDto quiz = quizService.createQuiz(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(quiz);
    }

}
