package com.twentyone.steachserver.domain.curriculum.controller;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CurriculumDetailResponse> createCurriculum(@AuthenticationPrincipal LoginCredential credential,
                                                                     @RequestBody CurriculumAddRequest request) {
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(credential, request);

        return ResponseEntity.ok(curriculumDetailResponse);
    }

    //커리큘럼 수강신청
    @PostMapping("/{curricula_id}/apply")
    public ResponseEntity<Void> registration(@AuthenticationPrincipal LoginCredential credential, @PathVariable("curricula_id") Integer curriculaId) {
        curriculumService.registration(credential, curriculaId);

        return ResponseEntity.ok().build(); //TODO 반환값
    }

    @GetMapping("/my-courses")
    public ResponseEntity<CurriculumListResponse> getMyCourses(@AuthenticationPrincipal LoginCredential credential) {
        CurriculumListResponse myCourses = curriculumService.getMyCourses(credential);

        return ResponseEntity.ok(myCourses); //TODO 반환값
    }
}
