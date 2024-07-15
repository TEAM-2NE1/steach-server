package com.twentyone.steachserver.domain.classroom.model;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classrooms")
@NoArgsConstructor
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sessionId;

    @OneToOne
    @JoinColumn(name = "lectures_id")
    private Lecture lecture;

    // Getters and Setters
}
