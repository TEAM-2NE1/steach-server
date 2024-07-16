package com.twentyone.steachserver.domain.statistic;

import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticController {
    private StatisticService statisticService;

    @GetMapping
    public ResponseEntity<?> getStatistic() {
        StatisticsDto statistics = statisticService.getStatistics();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(statistics);
    }
}
