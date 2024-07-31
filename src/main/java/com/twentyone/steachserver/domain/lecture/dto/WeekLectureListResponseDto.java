package com.twentyone.steachserver.domain.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

//네이밍 나중에 변경
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WeekLectureListResponseDto {
    private Integer lectureCount;
    private Integer weekCount;
    private Map<Integer, List<LectureResponseDto>> lectures;

    public static WeekLectureListResponseDto of(Map<Integer, List<LectureResponseDto>> lectures, Integer lectureCount) {
        WeekLectureListResponseDto dto = new WeekLectureListResponseDto();
        dto.lectureCount = lectureCount;
        dto.weekCount = lectures.size();
        dto.lectures = lectures;
        return dto;
    }
}
