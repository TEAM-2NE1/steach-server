package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Integer> {
}
