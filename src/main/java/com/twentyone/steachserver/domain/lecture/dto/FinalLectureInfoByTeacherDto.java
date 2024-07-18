package com.twentyone.steachserver.domain.lecture.dto;
import lombok.Getter;

import java.util.List;

@Getter
public class FinalLectureInfoByTeacherDto {
    private final List<StudentInfoByLectureDto> studentInfoByLectureDtoList;
    private Integer averageFocusRatio;
    private Integer averageSleepMinute;
    private Integer averageQuizScore;
    private Integer averageCorrectNumber;

    public FinalLectureInfoByTeacherDto(List<StudentInfoByLectureDto> studentInfoByLectureDtoList) {
        this.studentInfoByLectureDtoList = studentInfoByLectureDtoList;
    }

    public static FinalLectureInfoByTeacherDto createFinalLectureInfoByTeacherDto(List<StudentInfoByLectureDto> studentInfoByLectureDtoList) {
        FinalLectureInfoByTeacherDto finalLectureInfoByTeacherDto = new FinalLectureInfoByTeacherDto(studentInfoByLectureDtoList);

        int totalFocusRatio = 0;
        int totalSleepMinute = 0;
        int totalQuizScore = 0;
        int totalCorrectNumber = 0;

        for (StudentInfoByLectureDto studentInfo : studentInfoByLectureDtoList) {
            totalFocusRatio += studentInfo.getFocusRatio();
            totalSleepMinute += studentInfo.getSleepMinute();
            totalQuizScore += studentInfo.getTotalQuizScore();
            totalCorrectNumber += studentInfo.getCorrectNumber();
        }

        int studentCount = studentInfoByLectureDtoList.size();

        finalLectureInfoByTeacherDto.averageFocusRatio = studentCount > 0 ? totalFocusRatio / studentCount : 0;
        finalLectureInfoByTeacherDto.averageSleepMinute = studentCount > 0 ? totalSleepMinute / studentCount : 0;
        finalLectureInfoByTeacherDto.averageQuizScore = studentCount > 0 ? totalQuizScore / studentCount : 0;
        finalLectureInfoByTeacherDto.averageCorrectNumber = studentCount > 0 ? totalCorrectNumber / studentCount : 0;

        return finalLectureInfoByTeacherDto;
    }
}
