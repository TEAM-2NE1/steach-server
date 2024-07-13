package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "students_curricula")
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
