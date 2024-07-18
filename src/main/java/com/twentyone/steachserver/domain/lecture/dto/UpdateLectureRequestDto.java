package com.twentyone.steachserver.domain.lecture.dto;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class UpdateLectureRequestDto {
    private String lectureOrder;
    private String title;
    private LocalDateTime lectureStartTime;
}
