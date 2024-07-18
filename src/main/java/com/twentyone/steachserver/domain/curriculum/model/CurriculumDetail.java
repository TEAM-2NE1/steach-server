package com.twentyone.steachserver.domain.curriculum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "curriculum_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CurriculumDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 1000) //varchar(10000)
    private String subTitle;

    @Column(length = 1000) //varchar(10000)
    private String intro;

    @Column(length = 255) //varchar(10000)
    private String subCategory;

    @Lob
    @Column(length = 100000) //mediumtext
    private String information;

    @Column(name = "banner_img_url", length = 1000)
    private String bannerImgUrl;

    @Column(name = "weekdays_bitmask", columnDefinition = "BIT(7)")
    private byte weekdaysBitmask;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime lectureStartTime;
    private LocalTime lectureCloseTime;
}
