package com.twentyone.steachserver.domain.lecture.dto.update;


import java.time.LocalTime;

public record UpdateLectureRequestDto(String lectureTitle, LocalTime lectureStartTime, LocalTime lectureEndTime) {
}
