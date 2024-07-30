package com.twentyone.steachserver.domain.quiz.validator;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizChoiceValidator {

    public void validateQuizChoices(List<String> choices, List<String> answers) {
        validateNull(choices, answers);
        validateEmptyList(choices, "Choices cannot be empty");
        validateEmptyList(answers, "Answers cannot be empty");
        if (answers.size() > choices.size()) {
            throw new IllegalArgumentException("Answers cannot be more than choices");
        }
    }

    public static void validateNull(List<String> choices, List<String> answers) {
        if (choices == null) {
            throw new NullPointerException("Choices cannot be empty");
        }
        if (answers == null) {
            throw new NullPointerException("Answers cannot be empty");
        }
    }

    public void validateEmptyList(List<String> list, String Answers_cannot_be_empty) {
        if (list.isEmpty()) {
            throw new NullPointerException(Answers_cannot_be_empty);
        }
    }

    public void validateRightAnswers(int answerCount) {
        if (answerCount == 0) {
            throw new IllegalArgumentException("Answers cannot be empty");
        }
    }

}
