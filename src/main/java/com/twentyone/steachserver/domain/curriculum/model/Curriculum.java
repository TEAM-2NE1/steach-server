package com.twentyone.steachserver.domain.curriculum.model;

import com.twentyone.steachserver.domain.CurriculumDetail;
import com.twentyone.steachserver.domain.CurriculumInformation;
import com.twentyone.steachserver.domain.CurriculumSchedule;
import com.twentyone.steachserver.domain.Teacher;
import com.twentyone.steachserver.domain.enums.CurriculaCategory;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curricula")
@NoArgsConstructor
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "information_id")
    private CurriculumInformation information;

    @OneToOne
    @JoinColumn(name = "detail_id")
    private CurriculumDetail detail;

    private String title; //varchar(255)

    @Enumerated(EnumType.STRING)
    private CurriculaCategory category;

}