package com.twentyone.steachserver.domain.curricula.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "curriculum_details")
@NoArgsConstructor
public class CurriculumDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10000, columnDefinition = "TEXT", nullable = false) //varchar(10000)
    private String subTitle;

    @Column(length = 10000, columnDefinition = "TEXT", nullable = false) //varchar(10000)
    private String intro;

    @Column(length = 10000, columnDefinition = "TEXT", nullable = false) //varchar(10000)
    private String target;

    @Column(length = 10000, columnDefinition = "TEXT", nullable = false) //varchar(10000)
    private String requirement;

    @Lob
    @Column(length = 10000000, columnDefinition = "MEDIUMTEXT", nullable = false) //mediumtext
    private String information;

    @Column(name = "banner_img_url", length = 1000, nullable = false)
    private String bannerImgUrl;

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