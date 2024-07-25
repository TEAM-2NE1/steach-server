package com.twentyone.steachserver.domain.statistic.controller;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.dto.LectureStatisticsByAllStudentDto;
import com.twentyone.steachserver.domain.statistic.dto.LectureStatisticsByAllStudentListDto;
import com.twentyone.steachserver.domain.statistic.dto.RadarChartStatisticDto;
import com.twentyone.steachserver.domain.statistic.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "통계")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @Secured("ROLE_STUDENT")
    @Operation(summary = "레이더 차트 통계 정보 반환 ", description = "무조건 200을 반환")
    @GetMapping("/radar-chart")
    public ResponseEntity<RadarChartStatisticDto> getRadarChartStatistic(@AuthenticationPrincipal Student student) {
        RadarChartStatisticDto statistics = statisticService.getRadarChartStatistic(student.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(statistics);
    }

    @Operation(summary = "GPT 문장 반환 ", description = "무조건 200을 반환")
    @GetMapping("/gpt")
    public ResponseEntity<String> getGPTStatistic(@AuthenticationPrincipal Student student) {
        String gptString = statisticService.createGPTString(student);
        return ResponseEntity.ok()
                .body(gptString);
    }

    @Operation(summary = "강의에 대한 전체 학생에 대한 통계 반환 ", description = "무조건 200을 반환")
    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<LectureStatisticsByAllStudentListDto> getLectureStatisticsByAllStudent(@PathVariable("lectureId") Integer lectureId) {
        List<LectureStatisticsByAllStudentDto> statistics = statisticService.getLectureStatisticsByAllStudents(lectureId)
                .stream()
                .map(LectureStatisticsByAllStudentDto::of)
                .collect(Collectors.toList());

        if (statistics.isEmpty()) {
            throw new IllegalArgumentException("lectureId : " + lectureId + " 통계가 존재하지 않습니다.");
        }

        return ResponseEntity.ok(new LectureStatisticsByAllStudentListDto(statistics));
    }
}
