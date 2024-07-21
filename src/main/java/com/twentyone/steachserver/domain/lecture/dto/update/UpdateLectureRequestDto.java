package com.twentyone.steachserver.domain.lecture.dto.update;

import java.time.LocalDateTime;

public record UpdateLectureRequestDto(String lectureOrder, String lectureTitle, LocalDateTime lectureStartTime) {
}
