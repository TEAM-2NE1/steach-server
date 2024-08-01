package com.twentyone.steachserver.domain.curriculum.model;

import com.twentyone.steachserver.util.converter.WeekdayBitmaskUtil;
import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalTime lectureStartTime; //LocalTIme맞으니까 만지지마 주효림
    private LocalTime lectureCloseTime;

    @Builder.Default
    @Column(name = "current_attendees", columnDefinition = "TINYINT(4)")
    private Integer currentAttendees = 0; //현재 수강확정인원

    @Builder.Default
    @Column(columnDefinition = "TINYINT(4)")
    private Integer maxAttendees = 4; //수강정원

    public void register() {
        this.currentAttendees++;
    }

    public void update(
            String subTitle, String intro, String information, String subCategory, String bannerImgUrl,
            LocalDate startDate, LocalDate endDate, String weekdaysBitmask, LocalTime lectureStartTime,
            LocalTime lectureEndTime, int maxAttendees) {
        this.subTitle = subTitle;
        this.intro = intro;
        this.information = information;
        this.subCategory = subCategory;
        this.bannerImgUrl = bannerImgUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekdaysBitmask = WeekdayBitmaskUtil.convert(weekdaysBitmask);
        this.lectureStartTime = lectureStartTime;
        this.lectureCloseTime = lectureEndTime;
        this.maxAttendees = maxAttendees;
    }
}
