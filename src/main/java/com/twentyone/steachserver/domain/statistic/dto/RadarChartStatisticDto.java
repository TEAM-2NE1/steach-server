package com.twentyone.steachserver.domain.statistic.dto;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * item1: 100,
 * item2: 76,
 * item3: 88,
 * item4: 11,
 * item5: 63,
 * item6: 39,
 * item7: 46,
 */

public record RadarChartStatisticDto(Map<String, Integer> scores) {
    public static RadarChartStatisticDto of(Map<String, Integer> scores) {
        return new RadarChartStatisticDto(scores);
    }
}
