package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;

import java.util.List;

public class QuizChoiceServiceImpl implements QuizChoiceService{
    @Override
    public void createQuizChoices(List<String> choices, List<String> answers, Quiz savedQuiz) throws Exception{
        if (savedQuiz == null) {
            throw new NullPointerException("Quiz cannot be null");
        }

        if (choices == null || choices.isEmpty()) {
            throw new NullPointerException("Choices cannot be empty");
        }

        if (answers == null || answers.isEmpty()) {
            throw new NullPointerException("Answers cannot be empty");
        }

        if (answers.size() > choices.size()) {
            throw new IllegalArgumentException("Answers cannot be more than choices");
        }

        int answerCount = 0;
        for (String option : choices) {
            boolean isAnswer = answers.contains(option);
            if (isAnswer) answerCount++;
            QuizChoice.createQuizChoice(option, savedQuiz, isAnswer);
        }

        if (answerCount == 0) {
            throw new IllegalArgumentException("Answers cannot be empty");
        }
    }
}
