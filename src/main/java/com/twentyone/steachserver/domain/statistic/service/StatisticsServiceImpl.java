package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{
    private StatisticsRepository statisticsRepository;

    @Override
    public StatisticsDto getStatistics(){
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setItem1();
        statisticsDto.setItem2();
        statisticsDto.setItem3();
        statisticsDto.setItem4();
        statisticsDto.setItem5();
        statisticsDto.setItem6();
        statisticsDto.setItem7();

        return statisticsDto;
    }
}
