package com.twentyone.steachserver.domain;

import com.twentyone.steachserver.domain.student.model.Student;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(
        name = "students_curricula",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_curricula",
                        columnNames = {"curricula_id", "students_id"}
                )
        }
)
public class StudentsCurricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "curricula_id")
    private Curricula curricula;

    @ManyToOne
    @JoinColumn(name = "students_id")
    private Student student;
}
