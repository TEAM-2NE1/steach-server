package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LectureResponseDto {
    private Integer lectureId;
    private String lectureTitle;
    private Integer lectureOrder;
    private LocalDateTime lectureStartTime;

    public static LectureResponseDto fromDomain(Lecture lecture) {
        return LectureResponseDto.builder()
                .lectureId(lecture.getId())
                .lectureTitle(lecture.getTitle())
                .lectureOrder(lecture.getLectureOrder())
                .lectureStartTime(lecture.getLectureStartDate())
                .build();
    }
}
