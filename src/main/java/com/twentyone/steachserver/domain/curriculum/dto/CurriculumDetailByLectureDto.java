package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.lecture.model.Lecture;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record CurriculumDetailByLectureDto(LocalTime lectureCloseTime, String information, String bannerImgUrl) {
    public static CurriculumDetailByLectureDto createCurriculumDetailByLectureDto(CurriculumDetail curriculumDetail) {
        return new CurriculumDetailByLectureDto(
        curriculumDetail.getLectureCloseTime(),
        curriculumDetail.getInformation(),
                curriculumDetail.getBannerImgUrl());
    }
}
