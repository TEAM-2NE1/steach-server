package com.twentyone.steachserver.domain.lecture.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LectureRequestDto {
    private String title;
    private Integer lectureOrder;
    private LocalDateTime lectureStartTime;
}
