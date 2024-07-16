package com.twentyone.steachserver.domain.curriculum.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "curriculum_details")
@NoArgsConstructor
public class CurriculumDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lectureStartTime;
    private LocalDateTime lectureCloseTime;

    @Column(name = "banner_img_url", length = 1000)
    private String bannerImgUrl;

    @Column(length = 10000) //varchar(10000)
    private String intro;

    @Column(length = 10000) //varchar(10000)
    private String requirement;

    @Column(length = 10000) //varchar(10000)
    private String subTitle;

    @Column(length = 10000) //varchar(10000)
    private String target;

    @Column(length = 10000000) //mediumtext
    private String information;

    @Column(name = "weekdays_bitmask", columnDefinition = "BIT(7)")
    private byte weekdaysBitmask;


}
