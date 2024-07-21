package com.twentyone.steachserver.domain.studentLecture.dto;

import java.math.BigDecimal;


public record LectureStatisticsByAllStudentDto(Integer averageQuizTotalScore,
                                               Integer averageQuizAnswerCount,
                                               Integer averageFocusTime,
                                               BigDecimal averageFocusRatio) {
    public static LectureStatisticsByAllStudentDto of(Integer averageQuizTotalScore,
                                                      Integer averageQuizAnswerCount,
                                                      Integer averageFocusTime,
                                                      BigDecimal averageFocusRatio) {
        return new LectureStatisticsByAllStudentDto(averageQuizTotalScore, averageQuizAnswerCount, averageFocusTime, averageFocusRatio);
    }
}
