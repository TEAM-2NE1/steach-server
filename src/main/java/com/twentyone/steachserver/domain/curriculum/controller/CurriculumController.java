package com.twentyone.steachserver.domain.curriculum.controller;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curricula")
@RequiredArgsConstructor
public class CurriculumController {
    private final CurriculumService curriculumService;

    @GetMapping("/{id}")
    public ResponseEntity<CurriculumDetailResponse> getDetail(@PathVariable(name = "id") Integer id) {
        CurriculumDetailResponse detail = curriculumService.getDetail(id);
        return ResponseEntity.ok(detail);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<CurriculumDetailResponse> createCurriculum(@AuthenticationPrincipal LoginCredential credential,
                                                                     @RequestBody CurriculumAddRequest request) {
        curriculumService.create(credential, request);

        return ResponseEntity.ok().build();
    }
}
