package com.twentyone.steachserver.domain.statistic.dto;

import com.twentyone.steachserver.domain.statistic.model.mongoDB.LectureStatisticsByAllStudent;

import java.math.BigDecimal;

public record LectureStatisticsByAllStudentDto(Integer lectureId,
                                               Integer averageQuizTotalScore,
                                               Integer averageQuizAnswerCount,
                                               Integer averageFocusTime,
                                               BigDecimal averageFocusRatio) {
    public static LectureStatisticsByAllStudentDto of(LectureStatisticsByAllStudent lectureStatisticsByAllStudent) {
        return new LectureStatisticsByAllStudentDto(
                lectureStatisticsByAllStudent.getLectureId(),
                lectureStatisticsByAllStudent.getAverageQuizTotalScore(),
                lectureStatisticsByAllStudent.getAverageQuizAnswerCount(),
                lectureStatisticsByAllStudent.getAverageFocusTime(),
                lectureStatisticsByAllStudent.getAverageFocusRatio()
        );
    }
}
