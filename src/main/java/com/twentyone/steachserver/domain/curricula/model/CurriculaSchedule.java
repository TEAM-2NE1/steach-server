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
@Table(name = "curriculum_schedules")
@NoArgsConstructor
public class CurriculaSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "weekdays_bitmask", nullable = false, columnDefinition = "BIT(7) DEFAULT 0")
    private byte[] weekdaysBitmask;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "lecture_start_time", nullable = false)
    private LocalTime lectureStartTime;

    @Column(name = "lecture_close_time", nullable = false)
    private LocalTime lectureCloseTime;
}
