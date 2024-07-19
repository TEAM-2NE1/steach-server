package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompletedLecturesResponseDto extends LectureBeforeStartingResponseDto{
    private LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto;
    private List<StudentInfoByLectureDto> studentsQuizzesByLectureDto;

    private LocalDateTime realStartTime;
    private LocalDateTime realEndTime;

    private final Boolean isCompleted = true;

    protected CompletedLecturesResponseDto() {
        super();
    };

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
