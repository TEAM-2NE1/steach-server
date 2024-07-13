package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;

import java.util.List;

public class QuizChoiceServiceImpl implements QuizChoiceService{
    @Override
    public void createQuizChoices(List<String> choices, List<String> answers, Quiz savedQuiz) {
        for (String option : choices) {
            boolean isAnswer = answers.contains(option);
            QuizChoice.createQuizChoice(option, savedQuiz, isAnswer);
        }
    }
}
