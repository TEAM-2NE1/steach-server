package com.twentyone.steachserver.domain.member.memberController;

import com.twentyone.steachserver.domain.member.dto.StudentInfoRequest;
import com.twentyone.steachserver.domain.member.dto.StudentInfoResponse;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "학생 회원정보 조회")
    @GetMapping
    public ResponseEntity<StudentInfoResponse> getInfo(@AuthenticationPrincipal Student student) {
        StudentInfoResponse response = studentService.getInfo(student);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "학생 회원정보 수정")
    @PatchMapping
    public ResponseEntity<StudentInfoResponse> updateInfo(@RequestBody StudentInfoRequest request, @AuthenticationPrincipal Student student) {
        return ResponseEntity.ok(studentService.updateInfo(request, student));
    }
}
