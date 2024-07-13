package com.twentyone.steachserver.domain;

import com.twentyone.steachserver.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@Entity
@Table(name = "lectures_students")
public class LecturesStudents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "students_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lectures_id")
    private Lecture lecture;

    private Integer focusRatio;
}
