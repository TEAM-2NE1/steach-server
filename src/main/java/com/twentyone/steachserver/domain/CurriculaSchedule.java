package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "curricula_schedules")
@NoArgsConstructor
public class CurriculaSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer monday;
    private Integer tuesday;
    private Integer wednesday;
    private Integer thursday;
    private Integer friday;
    private Integer saturday;
    private Integer sunday;
    private Date startedDate;
    private Date closedDate;
    private Date startedTime;
    private Date closedTime;
}
