package com.twentyone.steachserver.domain.statistic.controller;

import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private StatisticService statisticService;

    @Operation(summary = "통계 정보 반환 ", description = "무조건 200을 반환")
    @GetMapping
    public ResponseEntity<?> getStatistic(String studentUsername) {
        StatisticsDto statistics = statisticService.getStatistics(studentUsername);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(statistics);
    }
}
