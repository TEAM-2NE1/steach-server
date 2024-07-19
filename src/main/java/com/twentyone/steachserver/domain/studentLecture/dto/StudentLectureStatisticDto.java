package com.twentyone.steachserver.domain.studentLecture.dto;

import java.math.BigDecimal;


public record StudentLectureStatisticDto(Integer averageQuizTotalScore,
                                         Integer averageQuizAnswerCount,
                                         Integer averageFocusTime,
                                         BigDecimal averageFocusRatio) {
    public static StudentLectureStatisticDto of(Integer averageQuizTotalScore,
                                                Integer averageQuizAnswerCount,
                                                Integer averageFocusTime,
                                                BigDecimal averageFocusRatio) {
        return new StudentLectureStatisticDto(averageQuizTotalScore, averageQuizAnswerCount, averageFocusTime, averageFocusRatio);
    }
}
