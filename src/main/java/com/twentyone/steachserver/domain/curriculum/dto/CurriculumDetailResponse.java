package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumDetailResponse {
    private String title;
    private String subTitle;
    private String intro;
    private String information;
    private CurriculumCategory category;
    private String subCategory;
    private String bannerImgUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String weekdaysBitmask;
    private int lectureStartTime;
    private int lectureEndTime;
    private int currentAttendees;
    private int maxAttendees;

    public static CurriculumDetailResponse fromDomain(Curriculum curriculum) {
        // 7을 이진수 문자열로 변환
        CurriculumDetail curriculumDetail = curriculum.getCurriculumDetail();
        String weekDaysBitmaskString = Integer.toBinaryString(curriculumDetail.getWeekdaysBitmask());

        // 길이가 7이 되도록 0으로 패딩
        String paddedWeekDaysBitmask = String.format("%0" + 7 + "d", Integer.parseInt(weekDaysBitmaskString));

        return CurriculumDetailResponse.builder()
                .title(curriculum.getTitle())
                .subTitle(curriculumDetail.getSubTitle())
                .intro(curriculumDetail.getIntro())
                .information(curriculumDetail.getInformation())
                .category(curriculum.getCategory())
                .subCategory(curriculumDetail.getSubCategory())
                .bannerImgUrl(curriculumDetail.getBannerImgUrl())
                .startDate(curriculumDetail.getStartDate())
                .endDate(curriculumDetail.getEndDate() != null ? curriculumDetail.getEndDate() : null)
                .weekdaysBitmask(paddedWeekDaysBitmask)
                .lectureStartTime(curriculumDetail.getLectureStartTime().getHour())
                .lectureEndTime(curriculumDetail.getLectureCloseTime().getHour())
                .currentAttendees(curriculumDetail.getCurrentAttendees())
                .maxAttendees(curriculumDetail.getMaxAttendees())
                .build();
    }
}
