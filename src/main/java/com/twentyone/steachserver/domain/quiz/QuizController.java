package com.twentyone.steachserver.domain.quiz;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
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

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizRequestDto request) throws Exception {
            Optional<QuizResponseDto> quiz = quizService.createQuiz(request);
            return quiz.map(ResponseEntity.status(HttpStatus.CREATED)::body)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponseDto> getQuizResponseDto(@PathVariable Integer quizId) {
        Optional<QuizResponseDto> quizOptional = quizService.getQuizResponseDto(quizId);
        return quizOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
