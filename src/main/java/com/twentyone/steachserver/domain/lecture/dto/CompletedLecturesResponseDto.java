package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailByLectureDto;
import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.dto.StudentByLectureDto;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompletedLecturesResponseDto extends LectureBeforeStartingResponseDto{
//    private LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto;

    private final List<StudentInfoByLectureDto> studentsQuizzesByLectureDto = new ArrayList<>();

    private LocalDateTime realStartTime;
    private LocalDateTime realEndTime;

    public CompletedLecturesResponseDto(Lecture lecture, SimpleCurriculumByLectureDto curriculumInfo, CurriculumDetailByLectureDto curriculumDetailInfo, List<StudentByLectureDto> studentDto) {
        super(lecture, curriculumInfo, curriculumDetailInfo, studentDto);
    }
}
