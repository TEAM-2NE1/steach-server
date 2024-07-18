package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CurriculumDetailByLectureDto {
    private final LocalDateTime estimatedEndTime;
    private final String information;
    private final String bannerImgUrl;

    public CurriculumDetailByLectureDto(CurriculumDetail curriculumDetail) {
        this.estimatedEndTime = curriculumDetail.getEndDate();
        this.information = curriculumDetail.getInformation();
        this.bannerImgUrl = curriculumDetail.getBannerImgUrl();
    }
}
