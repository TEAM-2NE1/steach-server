package com.twentyone.steachserver.domain.curriculum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "curriculum_details")
@NoArgsConstructor
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

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lectureStartTime;
    private LocalDateTime lectureCloseTime;


}
