package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;

public interface StatisticService {
    StatisticsDto getStatistics(String studentUsername);
}
