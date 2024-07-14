package com.twentyone.steachserver.domain.studentsQuizzes;

import com.twentyone.steachserver.domain.Student;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.*;

@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "students_quizzes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_students_quizzes",
                        columnNames = {"students_id", "quizzes_id"}
                )
        }
)
public class StudentsQuizzes {

    @ManyToOne
    @JoinColumn(name = "students_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "quizzes_id")
    private Quiz quiz;

    @Column(name = "total_score")
    private Integer totalScore;

    public void updateScore(Integer totalScore) {
        this.setTotalScore(totalScore);
    }
}
