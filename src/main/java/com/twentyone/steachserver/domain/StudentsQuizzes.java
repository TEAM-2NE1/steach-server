package com.twentyone.steachserver.domain;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "students_quizzes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_quiz",
                        columnNames = {"students_id", "quizzes_id"}
                )
        }
)
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
