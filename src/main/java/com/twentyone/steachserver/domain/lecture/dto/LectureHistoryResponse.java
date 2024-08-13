package com.twentyone.steachserver.domain.lecture.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureHistoryResponse {
    private String curriculumName;
    private String lectureName;
    private LocalDateTime lectureStartTime;
    private LocalDateTime lectureEndTime;
    private BigDecimal averageFocusRatio;
    private Integer averageFocusMinute;
    private Integer quizScore;
    private Integer totalQuizScore;
    private Integer quizCorrectNumber;
    private Integer quizTotalCount;
}
