package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;

import java.time.LocalDateTime;

public record SimpleCurriculumDto(String image,
                                  String title,
                                  String intro,
                                  Integer maxAttendees,
                                  Integer currentAttendees,
                                  LocalDateTime createdAt,
                                  String teacherName) {
    public static SimpleCurriculumDto of(Curriculum curriculum, String teacherName) {
        return new SimpleCurriculumDto(
                curriculum.getCurriculumDetail().getBannerImgUrl(),
                curriculum.getTitle(),
                curriculum.getCurriculumDetail().getIntro(),
                curriculum.getCurriculumDetail().getMaxAttendees(),
                curriculum.getCurriculumDetail().getCurrentAttendees(),
                curriculum.getCreatedAt(),
                teacherName);
    }
}
