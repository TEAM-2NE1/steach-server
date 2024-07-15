package com.twentyone.steachserver.domain.quiz.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quiz_choices")
@NoArgsConstructor
@Getter(value = AccessLevel.PRIVATE)
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
        QuizChoice choice = new QuizChoice();
        choice.setChoiceSentence(choiceText);
        choice.setQuiz(savedQuiz);
        choice.setIsAnswer(isAnswer ? 1 : 0);
    }

}

