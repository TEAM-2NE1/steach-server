package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.lecture.model.Lecture;

import java.time.LocalDateTime;

public record CurriculumDetailByLectureDto(LocalDateTime estimatedEndTime, String information, String bannerImgUrl) {
    public static CurriculumDetailByLectureDto createCurriculumDetailByLectureDto(Lecture lecture, CurriculumDetail curriculumDetail) {
        return new CurriculumDetailByLectureDto(
        lecture.getLectureStartDate().with(curriculumDetail.getLectureCloseTime()),
        curriculumDetail.getInformation(),
                curriculumDetail.getBannerImgUrl());
    }
}
