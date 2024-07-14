package com.twentyone.steachserver.domain.curricula.model;

import com.twentyone.steachserver.domain.enums.CurriculaCategory;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.studentCurricula.model.StudentsCurricula;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "curricula")
@NoArgsConstructor
public class Curricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title; //varchar(255)

    @Enumerated(EnumType.STRING)
    private CurriculaCategory category;

    @OneToOne
    @JoinColumn(name = "informations_id")
    private CurriculaInformation information;

    @OneToOne
    @JoinColumn(name = "schedules_id")
    private CurriculaSchedule schedule;

    @ManyToOne
    @JoinColumn(name = "teachers_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "curricula")
    private Set<StudentsCurricula> studentsCurricula = new HashSet<>();

}
