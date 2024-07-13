package com.twentyone.steachserver.domain;

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

    @ManyToOne
    @JoinColumn(name = "informations_id")
    private CurriculaInformation information;

    @ManyToOne
    @JoinColumn(name = "schedules_id")
    private CurriculaSchedule schedule;

    private String title;
    private String category;
}
