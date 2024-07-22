package com.twentyone.steachserver.domain.curriculum.controller;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.*;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/curricula")
@RequiredArgsConstructor
public class CurriculumController {
    private final CurriculumService curriculumService;

    @GetMapping("/{id}")
    public ResponseEntity<CurriculumDetailResponse> getDetail(@PathVariable(name = "id") Integer id) {
        CurriculumDetailResponse detail = curriculumService.getDetail(id);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public ResponseEntity<CurriculumDetailResponse> createCurriculum(
            @AuthenticationPrincipal LoginCredential credential,
            @RequestBody CurriculumAddRequest request) {
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(credential, request);

        return ResponseEntity.ok(curriculumDetailResponse);
    }

    //커리큘럼 수강신청
    @PostMapping("/{curricula_id}/apply")
    public ResponseEntity<Void> registration(@AuthenticationPrincipal LoginCredential credential,
                                             @PathVariable("curricula_id") Integer curriculaId) {
        curriculumService.registration(credential, curriculaId);

        return ResponseEntity.ok().build(); //TODO 반환값
    }

    //커리큘럼 조회
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
}
