package com.twentyone.steachserver.domain.quiz;

import com.twentyone.steachserver.domain.Lecture;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quizzes")
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lectures_id")
    private Lecture lectures;

    private Integer quizNumber;
    private String question;
}
