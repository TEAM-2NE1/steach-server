package com.twentyone.steachserver.domain.studentsQuizzes;

import com.twentyone.steachserver.domain.Student;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    @EmbeddedId
    private StudentsQuizzesId id;

    @ManyToOne
    @JoinColumn(name = "students_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "quizzes_id")
    private Quiz quiz;

    @Column(name = "total_score")
    private Integer totalScore;

    protected StudentsQuizzes() {}

    public void updateScore(Integer totalScore) {
        this.setTotalScore(totalScore);
    }

    public void updateQuiz(Quiz quiz) {
        this.setQuiz(quiz);
    }
}
