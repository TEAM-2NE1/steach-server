package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.RadarChartStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RadarChartStatisticRepository extends JpaRepository<RadarChartStatistic, Integer> {
}
