package com.twentyone.steachserver.domain.curriculum.model;

import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.enums.CurriculaCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curricula")
@NoArgsConstructor
@Getter(value = AccessLevel.PUBLIC)
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    private CurriculaCategory category;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}