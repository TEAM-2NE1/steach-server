package com.twentyone.steachserver.domain.statistic.dto;

import java.math.BigDecimal;

public record StatisticsByCurriculumCategory(BigDecimal averageFocusRatio,
                                             Integer totalLectureMinute) {
}
