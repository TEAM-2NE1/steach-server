package com.twentyone.steachserver.domain.curriculum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/*
{
title,
sub_title, //서브타이틀
intro,  //짧은소개
infromation,
start_date,
end_date,
weekdays_bitmask,
leture_start_time.
lecture_end_time,
banner_img_url,
sub_category,
category
}
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumAddRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("sub_title")
    private String subTitle;

    @JsonProperty("intro")
    private String intro;

    @JsonProperty("information")
    private String information;

    @JsonProperty("category")
    private CurriculumCategory category;

    @JsonProperty("sub_category")
    private String subCategory;

    @JsonProperty("banner_img_url")
    private String bannerImgUrl;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    private LocalDateTime endDate;

    @Schema(description = "Lecture start time in HH:mm:ss format", example = "0100101")
    @JsonProperty("weekdays_bitmask")
    private String weekdaysBitmask;

    @Schema(description = "Lecture start time in HH:mm:ss format", example = "15:30:00")
    @JsonProperty("lecture_start_time")
    private LocalDateTime lectureStartTime;

    @Schema(description = "Lecture end time in HH:mm:ss format", example = "15:30:00")
    @JsonProperty("lecture_end_time")
    private LocalDateTime lectureEndTime;

    @JsonProperty("max_attendees")
    private int maxAttendees;
}
