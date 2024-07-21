package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;

import java.util.List;

public interface StatisticService {
    StatisticsDto getStatistics(Integer studentId);

    void createStatisticsByFinalLecture(Lecture lecture);
}
