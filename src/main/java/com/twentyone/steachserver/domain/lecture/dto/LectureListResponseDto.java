package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LectureListResponseDto {
    private List<LectureResponseDto> lectures;

    public static LectureListResponseDto fromDomainList(List<Lecture> lectures) {
        List<LectureResponseDto> lectureResponseDtoList = new ArrayList<>();

        for (Lecture lecture: lectures) {
            lectureResponseDtoList.add(LectureResponseDto.fromDomain(lecture));
        }

        return new LectureListResponseDto(lectureResponseDtoList);
    }
}
