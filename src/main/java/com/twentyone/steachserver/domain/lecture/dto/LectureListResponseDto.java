package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LectureListResponseDto {
    private List<LectureInfoResponseDto> lectures;

    public static LectureListResponseDto fromDomainList(List<Lecture> lectures) {
        List<LectureInfoResponseDto> lectureResponseDtoList = new ArrayList<>();

        for (Lecture lecture: lectures) {
            lectureResponseDtoList.add(LectureInfoResponseDto.fromDomain(lecture));
        }

        return new LectureListResponseDto(lectureResponseDtoList);
    }
}
