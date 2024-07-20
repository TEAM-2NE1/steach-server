package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumAddRequest {
    private String title;
    private String subTitle;
    private String intro;
    private String information;
    private CurriculumCategory category;
    private String subCategory;
    private String bannerImgUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Schema(description = "Lecture start time in HH:mm:ss format", example = "0100101")
    private String weekdaysBitmask;

    @Schema(description = "Lecture start time in HH:mm:ss format", example = "15:30:00")
    private LocalDateTime lectureStartTime;

    @Schema(description = "Lecture end time in HH:mm:ss format", example = "15:30:00")
    private LocalDateTime lectureEndTime;

    private int maxAttendees;
}
