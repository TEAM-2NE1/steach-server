package com.twentyone.steachserver.domain.curriculum.controller;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/curricula")
@RequiredArgsConstructor
public class CurriculumController {
    private final CurriculumService curriculumService;

    @GetMapping("/{id}")
    public ResponseEntity<CurriculumDetailResponse> getDetail(@AuthenticationPrincipal LoginCredential credential,
                                                              @PathVariable(name = "id") Integer id) {
        CurriculumDetailResponse detail = curriculumService.getDetail(id);
        return ResponseEntity.ok(detail);
    }
}
