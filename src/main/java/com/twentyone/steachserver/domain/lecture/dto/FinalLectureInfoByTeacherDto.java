package com.twentyone.steachserver.domain.lecture.dto;

import java.util.List;

public class FinalLectureInfoByTeacherDto {
    private List<StudentInfoByLectureDto> studentInfoByLectureDtoList;
    private Integer averageFocusRatio;
    private Integer averageSleepTime;
    private Integer averageQuizScore;
    private Integer averageCorrectNumber;
}
