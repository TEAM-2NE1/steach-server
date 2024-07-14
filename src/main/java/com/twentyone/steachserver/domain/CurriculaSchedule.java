package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

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
    private LocalDateTime startedAt;
    private LocalDateTime closedAt;
}
