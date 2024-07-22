package com.twentyone.steachserver.domain.member.memberController;

import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @Operation(summary = "선생님 회원정보 조회")
    @GetMapping
    public ResponseEntity<TeacherInfoResponse> getInfo(@AuthenticationPrincipal Teacher teacher) {
        TeacherInfoResponse response = teacherService.getInfo(teacher);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "선생님 회원정보 수정")
    @PatchMapping
    public ResponseEntity<TeacherInfoResponse> updateInfo(@RequestBody TeacherInfoRequest request, @AuthenticationPrincipal Teacher teacher) {
        return ResponseEntity.ok(teacherService.updateInfo(request, teacher));
    }
}
