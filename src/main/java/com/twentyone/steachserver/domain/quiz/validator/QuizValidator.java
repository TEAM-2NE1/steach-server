package com.twentyone.steachserver.domain.quiz.validator;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Component
public class QuizValidator {
    public void validateEmptyQuiz(Quiz quiz) {
        if (quiz == null) {
            throw new NullPointerException("Quiz cannot be empty");
        }
    }


    public void validateEmptyQuiz(Optional<Quiz> quiz) {
        if (quiz.isEmpty()) {
            throw new NullPointerException("Quiz cannot be empty");
        }
    }
}
