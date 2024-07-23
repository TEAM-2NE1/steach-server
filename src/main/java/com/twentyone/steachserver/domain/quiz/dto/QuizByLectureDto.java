package com.twentyone.steachserver.domain.quiz.dto;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import jakarta.persistence.Column;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record QuizByLectureDto(Integer quizId,
                               Integer quizNumber,
                               String question,
                               List<String> choiceSentences,
                               List<String> answers
) {
    public static QuizByLectureDto of(Quiz quiz) {
        List<QuizChoice> quizChoices = quiz.getQuizChoices();
        List<String> choiceSentences = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        for (QuizChoice quizChoice : quizChoices) {
            String choiceSentence = quizChoice.getChoiceSentence();
            choiceSentences.add(choiceSentence);
            if (quizChoice.getIsAnswer()) {
                answers.add(choiceSentence);
            }
        }
        return new QuizByLectureDto(quiz.getId(), quiz.getQuizNumber(), quiz.getQuestion(), choiceSentences, answers);
    }
}
