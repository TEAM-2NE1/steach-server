package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import lombok.Getter;

import java.time.LocalDateTime;

public record CurriculumDetailByLectureDto(LocalDateTime estimatedEndTime, String information, String bannerImgUrl) {
    public static CurriculumDetailByLectureDto createCurriculumDetailByLectureDto(CurriculumDetail curriculumDetail) {
        return new CurriculumDetailByLectureDto(
                curriculumDetail.getEndDate(),
                curriculumDetail.getInformation(),
                curriculumDetail.getBannerImgUrl());
    }
}
