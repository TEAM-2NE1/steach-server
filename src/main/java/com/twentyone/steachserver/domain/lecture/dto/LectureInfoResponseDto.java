package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LectureInfoResponseDto {
    private Integer lectureId;
    private String title;
    private Integer lectureOrder;
    private LocalDateTime lectureStartDate; //시작 날짜로 해석하겠음 - 주효림
    private LocalDateTime realStartTime;
    private LocalDateTime realEndTime;

    public static LectureInfoResponseDto fromDomain(Lecture lecture) {
        return LectureInfoResponseDto.builder()
                .lectureId(lecture.getId())
                .title(lecture.getTitle())
                .lectureOrder(lecture.getLectureOrder())
                .lectureStartDate(lecture.getLectureStartDate())
                .realStartTime(lecture.getRealStartTime())
                .realEndTime(lecture.getRealEndTime())
                .build();
    }
}
