package com.twentyone.steachserver.domain;

import com.twentyone.steachserver.domain.enums.CurriculaCategory;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curricula")
@NoArgsConstructor
public class Curricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teachers_id")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "informations_id")
    private CurriculaInformation information;

    @OneToOne
    @JoinColumn(name = "schedules_id")
    private CurriculaSchedule schedule;

    private String title; //varchar(255)

    @Enumerated(EnumType.STRING)
    private CurriculaCategory category;
}
