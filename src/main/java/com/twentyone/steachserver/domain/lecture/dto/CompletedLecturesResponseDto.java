package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CompletedLecturesResponseDto extends LectureBeforeStartingResponseDto{
    private final LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto;
    private final List<StudentInfoByLectureDto> studentsQuizzesByLectureDto;

    private final LocalDateTime realStartTime;
    private final LocalDateTime realEndTime;

    private final Boolean isCompleted = true;

    private CompletedLecturesResponseDto(LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto,
                                         List<StudentInfoByLectureDto> studentsQuizzesByLectureDto,
                                         LocalDateTime realStartTime, LocalDateTime realEndTime) {
        this.lectureBeforeStartingResponseDto = lectureBeforeStartingResponseDto;
        this.studentsQuizzesByLectureDto = studentsQuizzesByLectureDto;
        this.realStartTime = realStartTime;
        this.realEndTime = realEndTime;
    }

    public static CompletedLecturesResponseDto of(LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto,
                    List<StudentInfoByLectureDto> studentsQuizzesByLectureDto,
                    Lecture lecture) {
        return new CompletedLecturesResponseDto(lectureBeforeStartingResponseDto, studentsQuizzesByLectureDto, lecture.getRealStartTime(),lecture.getRealEndTime());
    }

}
