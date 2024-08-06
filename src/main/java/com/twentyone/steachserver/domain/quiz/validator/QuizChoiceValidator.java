package com.twentyone.steachserver.domain.quiz.validator;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizChoiceValidator {

    public void validateQuizChoices(List<String> choices, List<String> answers) {
        validateNull(choices, "Choices");
        validateNull(answers, "Answers");
        validateEmptyList(choices, "Choices cannot be empty");
        validateEmptyList(answers, "Answers cannot be empty");
        validateAnswersSize(choices, answers);
    }

    private void validateNull(List<String> list, String name) {
        if (list == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }

    public void validateEmptyList(List<String> list, String Answers_cannot_be_empty) {
        if (list.isEmpty()) {
            throw new NullPointerException(Answers_cannot_be_empty);
        }
    }

    public void validateAnswersSize(List<String> choices, List<String> answers) {
        if (answers.size() > choices.size()) {
            throw new IllegalArgumentException("Answers cannot be more than choices");
        }
    }

    public void validateRightAnswers(int answerCount) {
        if (answerCount == 0) {
            throw new IllegalArgumentException("Answers cannot be empty");
        }
    }

}
