package com.twentyone.steachserver.domain.curriculum.controller;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.*;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.lecture.dto.LectureListResponseDto;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "커리큘럼")
@Slf4j
@RestController
@RequestMapping("/api/v1/curricula")
@RequiredArgsConstructor
public class CurriculumController {
    private final CurriculumService curriculumService;
    private final LectureService lectureService;

    @Operation(summary = "커리큘럼 단일조회!")
    @GetMapping("/{id}")
    public ResponseEntity<CurriculumDetailResponse> getDetail(@PathVariable(name = "id") Integer id) {
        CurriculumDetailResponse detail = curriculumService.getDetail(id);
        return ResponseEntity.ok(detail);
    }

    @Operation(summary = "[강사] 커리큘럼 생성!")
    @PostMapping
    public ResponseEntity<CurriculumDetailResponse> createCurriculum(
            @AuthenticationPrincipal LoginCredential credential,
            @RequestBody CurriculumAddRequest request) {
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(credential, request);

        return ResponseEntity.ok(curriculumDetailResponse);
    }

    @Operation(summary = "[학생] 커리큘럼 수강신청!")
    @PostMapping("/{curricula_id}/apply")
    public ResponseEntity<Void> registration(@AuthenticationPrincipal LoginCredential credential,
                                             @PathVariable("curricula_id") Integer curriculaId) {
        curriculumService.registration(credential, curriculaId);

        return ResponseEntity.ok().build(); //TODO 반환값
    }

    @Operation(summary = "커리큘럼 리스트 조회/검색", description = "lecture_start_time 은 날짜시간 같이 나옵니다")
    @GetMapping
    public ResponseEntity<CurriculumListResponse> getCurricula(
            @RequestParam(value = "curriculum_category", required = false) CurriculumCategory curriculumCategory,
            @RequestParam(value = "order", required = false) CurriculaOrderType order,
            @RequestParam(value = "only_available", required = false) Boolean onlyAvailable,
            @RequestParam(value = "search", required = false) String search) {

        CurriculaSearchCondition condition = new CurriculaSearchCondition(curriculumCategory, order, onlyAvailable,
                search);

        CurriculumListResponse result = curriculumService.search(condition);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "인기 커리큘럼 조회", description = "lecture_start_time 은 날짜시간 같이 나옵니다")
    @GetMapping("/popular-per-ratio")
    public ResponseEntity<CurriculumListInOrderResponseDto> getPopularCurriculum(){
        List<SimpleCurriculumDto> result = curriculumService.getCurriculumListInOrder(CurriculaOrderType.POPULAR_PER_RATIO);
        return ResponseEntity.ok(CurriculumListInOrderResponseDto.of(result));
    }

    @Operation(summary = "최신 커리큘럼 조회", description = "lecture_start_time 은 날짜시간 같이 나옵니다")
    @GetMapping("/latest")
    public ResponseEntity<CurriculumListInOrderResponseDto> getLatestCurriculum(){
        List<SimpleCurriculumDto> result = curriculumService.getCurriculumListInOrder(CurriculaOrderType.LATEST);
        return ResponseEntity.ok(CurriculumListInOrderResponseDto.of(result));
    }

    @Operation(summary = "커리큘럼에 해당하는 강의 리스트 조회")
    @GetMapping("/{curriculum_id}/lectures")
    public ResponseEntity<LectureListResponseDto> getLecturesByCurriculum(
            @PathVariable("curriculum_id") Integer curriculumId) {
        LectureListResponseDto byCurriculum = lectureService.findByCurriculum(curriculumId);

        return ResponseEntity.ok(byCurriculum);
    }
}
