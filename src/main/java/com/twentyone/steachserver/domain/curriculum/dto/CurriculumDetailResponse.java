package com.twentyone.steachserver.domain.curriculum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.enums.CurriculaCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
{
  title:
  sub_title:
  intro: ,
  information:
  category:
  sub_category:
  banner_img_url: daf,
  start_date: 2020-07-14,
  end_date,
    weekdays_bitmask: 0100111,
    lecture_start_time: 07,
    lecture_end_time: 10,
}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumDetailResponse {

    @JsonProperty("title")
    private String title;

    @JsonProperty("sub_title")
    private String subTitle;

    @JsonProperty("intro")
    private String intro;

    @JsonProperty("information")
    private String information;

    @JsonProperty("category")
    private CurriculaCategory category;

    @JsonProperty("sub_category")
    private String subCategory;

    @JsonProperty("banner_img_url")
    private String bannerImgUrl;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("weekdays_bitmask")
    private String weekdaysBitmask;

    @JsonProperty("lecture_start_time")
    private int lectureStartTime;

    @JsonProperty("lecture_end_time")
    private int lectureEndTime;

    @JsonProperty("current_attendees")
    private int currentAttendees;

    @JsonProperty("max_attendees")
    private int maxAttendees;

    public static CurriculumDetailResponse fromDomain(Curriculum curriculum, CurriculumDetail curriculumDetail) {
        // 7을 이진수 문자열로 변환
        String weekDaysBitmaskString = Integer.toBinaryString(curriculumDetail.getWeekdaysBitmask());

        // 길이가 7이 되도록 0으로 패딩
        String paddedWeekDaysBitmask = String.format("%0" + 7 + "d", Integer.parseInt(weekDaysBitmaskString));

        return CurriculumDetailResponse.builder()
                .title(curriculum.getTitle())
                .subTitle(curriculumDetail.getSubTitle())
                .intro(curriculumDetail.getIntro())
                .information(curriculumDetail.getInformation())
                .category(curriculum.getCategory())
                .subCategory(curriculumDetail.getSubCategory())
                .bannerImgUrl(curriculumDetail.getBannerImgUrl())
                .startDate(curriculumDetail.getStartDate())
                .endDate(curriculumDetail.getEndDate() != null ? curriculumDetail.getEndDate() : null)
                .weekdaysBitmask(paddedWeekDaysBitmask)
                .lectureStartTime(curriculumDetail.getLectureStartTime().getHour())
                .lectureEndTime(curriculumDetail.getLectureCloseTime().getHour())
                .currentAttendees(curriculumDetail.getCurrentAttendees())
                .maxAttendees(curriculumDetail.getMaxAttendees())
                .build();
    }
}
