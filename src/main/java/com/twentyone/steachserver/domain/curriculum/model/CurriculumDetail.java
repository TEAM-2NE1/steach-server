package com.twentyone.steachserver.domain.curriculum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Entity
@Table(name = "curriculum_details")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(length = 10000) //mediumtext
    private String information;

    @Column(name = "banner_img_url", length = 1000)
    private String bannerImgUrl;

    @Column(name = "weekdays_bitmask", columnDefinition = "BIT(7)")
    private byte weekdaysBitmask;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime lectureStartTime;
    private LocalTime lectureCloseTime;

    @Builder.Default
    @Column(name = "current_attendees", columnDefinition = "TINYINT(4)")
    private Integer currentAttendees = 0; //현재 수강확정인원

    @Column(columnDefinition = "TINYINT(4)")
    private Integer maxAttendees; //수강정원

    public void register() {
        this.currentAttendees++;
    }
}
