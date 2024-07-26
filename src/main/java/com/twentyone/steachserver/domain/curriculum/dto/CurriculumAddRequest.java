package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDate startDate; //시작
    private LocalDate endDate;

    @Schema(description = "Lecture start time in HH:mm:ss format", example = "0100101")
    private String weekdaysBitmask;

    @Schema(description = "Lecture start time in HH:mm:ss format", example = "15:30")
    private LocalTime lectureStartTime; //TODO lectureStartTime < lectureEndTIme 처리해줘야하나?

    @Schema(description = "Lecture end time in HH:mm:ss format", example = "16:30")
    private LocalTime lectureEndTime;

    private int maxAttendees;
}
