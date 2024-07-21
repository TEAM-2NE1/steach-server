package com.twentyone.steachserver.domain.lecture.dto;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
public class FinalLectureInfoByTeacherDto {
    private final List<StudentInfoByLectureDto> studentInfoByLectureDtoList;
    private BigDecimal averageFocusRatio;
    private Integer averageSleepMinute;
    private Integer averageQuizScore;
    private Integer averageCorrectNumber;

    private FinalLectureInfoByTeacherDto(List<StudentInfoByLectureDto> studentInfoByLectureDtoList) {
        this.studentInfoByLectureDtoList = studentInfoByLectureDtoList;
    }

    public static FinalLectureInfoByTeacherDto createFinalLectureInfoByTeacherDto(List<StudentInfoByLectureDto> studentInfoByLectureDtoList) {
        FinalLectureInfoByTeacherDto finalLectureInfoByTeacherDto = new FinalLectureInfoByTeacherDto(studentInfoByLectureDtoList);

        BigDecimal totalFocusRatio = BigDecimal.valueOf(0);
        int totalFocusMinute = 0;
        int totalQuizScore = 0;
        int totalCorrectNumber = 0;

        for (StudentInfoByLectureDto studentInfo : studentInfoByLectureDtoList) {
            totalFocusRatio = totalFocusRatio.add(studentInfo.getFocusRatio());
            totalFocusMinute += studentInfo.getFocusMinute();
            totalQuizScore += studentInfo.getTotalQuizScore();
            totalCorrectNumber += studentInfo.getCorrectNumber();
        }

        int studentCount = studentInfoByLectureDtoList.size();

        finalLectureInfoByTeacherDto.averageFocusRatio = studentCount > 0 ? totalFocusRatio.divide(BigDecimal.valueOf(studentCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        finalLectureInfoByTeacherDto.averageSleepMinute = studentCount > 0 ? totalFocusMinute / studentCount : 0;
        finalLectureInfoByTeacherDto.averageQuizScore = studentCount > 0 ? totalQuizScore / studentCount : 0;
        finalLectureInfoByTeacherDto.averageCorrectNumber = studentCount > 0 ? totalCorrectNumber / studentCount : 0;

        return finalLectureInfoByTeacherDto;
    }
}
