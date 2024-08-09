package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;

import com.twentyone.steachserver.util.converter.DateTimeUtil;
import com.twentyone.steachserver.util.converter.WeekdayBitmaskUtil;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumDetailResponse {
    private Integer curriculumId;
    private Integer teacherId;
    private String teacherName;
    private String title;
    private String subTitle;
    private String intro;
    private String information;
    private CurriculumCategory category;
    private String subCategory;
    private String bannerImgUrl;
    private String startDate;
    private String endDate;
    private String weekdaysBitmask;
    private String lectureStartTime;
    private String lectureEndTime;
    private int currentAttendees;
    private int maxAttendees;
    private String createdAt;

    public static CurriculumDetailResponse fromDomain(Curriculum curriculum) {
        // 7을 이진수 문자열로 변환
        CurriculumDetail curriculumDetail = curriculum.getCurriculumDetail();

        return CurriculumDetailResponse.builder()
                .curriculumId(curriculum.getId())
                .teacherId(curriculum.getTeacher().getId())
                .teacherName(curriculum.getTeacher().getName())
                .title(curriculum.getTitle())
                .subTitle(curriculumDetail.getSubTitle())
                .intro(curriculumDetail.getIntro())
                .information(curriculumDetail.getInformation())
                .category(curriculum.getCategory())
                .subCategory(curriculumDetail.getSubCategory())
                .bannerImgUrl(curriculumDetail.getBannerImgUrl())
                .startDate(DateTimeUtil.convert(curriculumDetail.getStartDate()))
                .endDate(curriculumDetail.getEndDate() != null ? DateTimeUtil.convert(curriculumDetail.getEndDate()) : null)
                .weekdaysBitmask(WeekdayBitmaskUtil.convert(curriculumDetail.getWeekdaysBitmask()))
                .lectureStartTime(DateTimeUtil.convert(curriculumDetail.getLectureStartTime()))
                .lectureEndTime(DateTimeUtil.convert(curriculumDetail.getLectureCloseTime()))
                .currentAttendees(curriculumDetail.getCurrentAttendees())
                .maxAttendees(curriculumDetail.getMaxAttendees())
                .createdAt(DateTimeUtil.convert(curriculum.getCreatedAt()))
                .build();
    }
    public static CurriculumDetailResponse fromDomainBySimple(Curriculum curriculum) {
        // 7을 이진수 문자열로 변환
        CurriculumDetail curriculumDetail = curriculum.getCurriculumDetail();

        return CurriculumDetailResponse.builder()
                .curriculumId(curriculum.getId())
                .teacherId(curriculum.getTeacher().getId())
                .teacherName(curriculum.getTeacher().getName())
                .title(curriculum.getTitle())
                .intro(curriculumDetail.getIntro())
                .bannerImgUrl(curriculumDetail.getBannerImgUrl())
                .startDate(DateTimeUtil.convert(curriculumDetail.getStartDate()))
                .endDate(curriculumDetail.getEndDate() != null ? DateTimeUtil.convert(curriculumDetail.getEndDate()) : null)
                .weekdaysBitmask(WeekdayBitmaskUtil.convert(curriculumDetail.getWeekdaysBitmask()))
                .lectureStartTime(DateTimeUtil.convert(curriculumDetail.getLectureStartTime()))
                .lectureEndTime(DateTimeUtil.convert(curriculumDetail.getLectureCloseTime()))
                .currentAttendees(curriculumDetail.getCurrentAttendees())
                .maxAttendees(curriculumDetail.getMaxAttendees())
                .createdAt(DateTimeUtil.convert(curriculum.getCreatedAt()))
                .build();
    }
}
