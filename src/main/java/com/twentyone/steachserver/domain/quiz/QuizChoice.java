package com.twentyone.steachserver.domain.quiz;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_choices")
@NoArgsConstructor
public class QuizChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "quizzes_id")
    private Quiz quiz;

    private Boolean isAnswer;
    private String choiceSentence;
}

