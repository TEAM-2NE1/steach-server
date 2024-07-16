package com.twentyone.steachserver.domain.statistic;

import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<?> getStatistics() {
        StatisticsDto statistics = statisticsService.getStatistics();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(statistics);
    }
}
