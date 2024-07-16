package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private StatisticRepository statisticRepository;

    /**
     * Each item means a focus ratio for one category
     * (e.g. 76 for Korean, 81 for Math. 98 for Engineering)
     * Since category names are not settled, just returning 7 numbers.
     * 각 item은 focus ratio를 의미.
     * (예시. 국어 76, 수학 81, 공학 98)
     * 카테고리명이 아직 정해지지 않았기 때문에 숫자 7개의 숫자를 반환.
     * @return 7 numbers
     */
    @Override
    public StatisticsDto getStatistics(){
        StatisticsDto statisticsDto = new StatisticsDto();
        // 추후 열기
//        statisticsDto.setItem1();
//        statisticsDto.setItem2();
//        statisticsDto.setItem3();
//        statisticsDto.setItem4();
//        statisticsDto.setItem5();
//        statisticsDto.setItem6();
//        statisticsDto.setItem7();

        return statisticsDto;
    }
}
