package com.twentyone.steachserver.domain;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students_quizzes")
public class StudentsQuizzes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "students_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "quizzes_id")
    private Quiz quiz;

    private Integer totalScore;
}
