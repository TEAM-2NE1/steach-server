package com.twentyone.steachserver.domain.lecture.dto;

import lombok.Getter;

import java.time.LocalDateTime;



public record UpdateLectureRequestDto(String lectureOrder, String title, LocalDateTime lectureStartTime) {
}
