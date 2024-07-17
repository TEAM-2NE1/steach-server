package com.twentyone.steachserver.domain.curriculum.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String category;

    @JsonProperty("sub_category")
    private String subCategory;

    @JsonProperty("banner_img_url")
    private String bannerImgUrl;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("weekdays_bitmask")
    private byte weekdaysBitmask;

    @JsonProperty("lecture_start_time")
    private int lectureStartTime;

    @JsonProperty("lecture_end_time")
    private int lectureEndTime;
}
