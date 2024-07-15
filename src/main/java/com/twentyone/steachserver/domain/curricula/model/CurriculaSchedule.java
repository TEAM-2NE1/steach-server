package com.twentyone.steachserver.domain.curricula.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "curricula_schedules")
@NoArgsConstructor
public class CurriculaSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    private Boolean sunday;
    private LocalDate startedDate;
    private Integer weekNumber;
    private LocalTime started_time;
    private LocalTime closed_time;

    @OneToOne
    @JoinColumn(name = "curricula_id", referencedColumnName = "id")
    private Curricula curricula;

}
