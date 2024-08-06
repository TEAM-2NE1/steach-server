package com.twentyone.steachserver.interfaces.main;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.service.redis.CurriculumRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/main/curriculum")
@RequiredArgsConstructor
public class MainCurriculumController {

    private final CurriculumRedisService curriculumRedisService;

    @GetMapping("/popular-ratio")
    public ResponseEntity<CurriculumListResponse> getPopularRatioCurriculum() {
        return ResponseEntity.ok(CurriculumListResponse.of(curriculumRedisService.getPopularRatioCurriculum())); // curriculumRedisService.getPopularRatioCurriculum();
    }

    @GetMapping("/latest")
    public ResponseEntity<CurriculumListResponse> getLatestCurriculum() {
        return ResponseEntity.ok(CurriculumListResponse.of(curriculumRedisService.getLatestCurricula())); // curriculumRedisService.getPopularRatioCurriculum();
    }
}
