package com.twentyone.steachserver.domain.statistic.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


/**
 *   item1: 100,
 *   item2: 76,
 *   item3: 88,
 *   item4: 11,
 *   item5: 63,
 *   item6: 39,
 *   item7: 46,
 */

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class radarChartStatisticDto {
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;
    private Integer item7;
}
