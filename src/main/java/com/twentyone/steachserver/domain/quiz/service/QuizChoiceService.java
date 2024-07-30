package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.model.Quiz;

import java.util.List;

public interface QuizChoiceService {
    void createQuizChoices(List<String> choices, List<String> answers, Quiz savedQuiz) throws RuntimeException;
    List<String> getAnswers(Quiz quiz);
    List<String> getChoices(Quiz quiz);
}
