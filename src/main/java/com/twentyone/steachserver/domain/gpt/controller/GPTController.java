package com.twentyone.steachserver.domain.gpt.controller;

import java.util.HashMap;
import java.util.Map;

import com.twentyone.steachserver.domain.gpt.service.GPTService;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
public class GPTController {

    private final GPTService gptService;
    private final StatisticService statisticService;

    @Secured("ROLE_STUDENT")
    @PostMapping("/career")
    public ResponseEntity<?> getGPTResponse(@AuthenticationPrincipal Student student) {
        String gptString = null;
        Map<String, String> responseMap = new HashMap<>();
        try {
            gptString = statisticService.createGPTString(student);
            responseMap.put("gptString", gptString);
            String gptStatistic = gptService.getChatGPTResponse(gptString);
            responseMap.put("gptStatistic", gptStatistic);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " 데이터: " + responseMap);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
    }
}