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

    @Column(name = "is_answer", nullable = false)
    private Boolean isAnswer;

    @Column(name = "choice_sentence", nullable = false)
    private String choiceSentence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    public static QuizChoice createEmptyQuizChoice() {
        return new QuizChoice();
    }

    public static QuizChoice createQuizChoice(String choiceText, Quiz savedQuiz, boolean isAnswer) {
        QuizChoice quizChoice = new QuizChoice();
        quizChoice.setChoiceSentence(choiceText);
        quizChoice.setQuiz(savedQuiz);
        quizChoice.setIsAnswer(isAnswer);
        savedQuiz.addChoice(quizChoice);
        return quizChoice;
    }

    public void updateQuiz(Quiz quiz) {
        this.setQuiz(quiz);
    }
}
