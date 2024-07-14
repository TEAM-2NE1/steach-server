package com.twentyone.steachserver.domain.quiz.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quiz_choices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
public class QuizChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "quizzes_id")
    private Quiz quiz;

    private Integer isAnswer;
    private String choiceSentence;

    public static void createQuizChoice(String choiceText, Quiz savedQuiz, boolean isAnswer) {
        QuizChoice quizChoice = new QuizChoice();
        quizChoice.setChoiceSentence(choiceText);
        quizChoice.setQuiz(savedQuiz);
        quizChoice.setIsAnswer(isAnswer ? 1 : 0);
        savedQuiz.addChoice(quizChoice);
    }

    public void updateQuiz(Quiz quiz) {
        this.setQuiz(quiz);
    }
}

