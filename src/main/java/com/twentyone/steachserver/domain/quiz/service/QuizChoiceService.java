package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.model.Quiz;

import java.util.List;

public interface QuizChoiceService {
    void createQuizChoices(List<String> choices, String answers, Quiz savedQuiz);
    String getAnswers(Quiz quiz);
    List<String> getChoices(Quiz quiz);
}
