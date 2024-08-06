package com.twentyone.steachserver.domain.curriculum.service.batch;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.curriculum.service.redis.CurriculumRedisService;
import com.twentyone.steachserver.util.converter.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurriculumBatchService {

    private final CurriculumService curriculumService;
    private final CurriculumRedisService curriculumRedisService;


    // Todo: batch 처리를 통해서 빠르게 생성하기
    @Scheduled(fixedRate = 600000)  // 10분마다 실행
    public void updateCurriculumsInRedis() {
        log.info("Updating curriculums in Redis: {}", DateTimeUtil.convert(LocalDateTime.now()));
        List<CurriculumDetailResponse> popularRatioCurriculums = curriculumService.getPopularRatioCurriculums();
        curriculumRedisService.savePopularRatioCurricula(popularRatioCurriculums);

        List<CurriculumDetailResponse> latestCurriculums = curriculumService.getLatestCurriculums();
        curriculumRedisService.saveLatestCurricula(latestCurriculums);
    }
}
