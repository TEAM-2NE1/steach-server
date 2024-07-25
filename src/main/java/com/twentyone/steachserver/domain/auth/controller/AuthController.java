package com.twentyone.steachserver.domain.auth.controller;

import com.twentyone.steachserver.domain.auth.dto.*;
import com.twentyone.steachserver.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "인증")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "학생 회원가입!", description = "username은 빈칸 불가능, 숫자로 시작 불가능, 영어와 숫자 조합만 가능")
    @PostMapping("/student/join")
    public ResponseEntity<LoginResponseDto> signupStudent(@RequestBody StudentSignUpDto studentSignUpDto) {
        LoginResponseDto loginResponseDto = authService.signUpStudent(studentSignUpDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }

    @Operation(summary = "강사 회원가입!", description = "username은 빈칸 불가능, 숫자로 시작 불가능, 영어와 숫자 조합만 가능/ file은 첨부하지 않아도 됨(추후 변경예정) <br/> auth_code 사용안한 것만 가능")
    @PostMapping(value = "/teacher/join", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<LoginResponseDto> signupTeacher(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart("teacherSignUpDto") TeacherSignUpDto teacherSignUpDto) throws IOException {
        LoginResponseDto loginResponseDto = authService.signUpTeacher(teacherSignUpDto, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }

    @Operation(summary = "로그인!")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @Operation(summary = "아이디 중복확인")
    @GetMapping("/check-username/{username}")
    public ResponseEntity<CheckUsernameAvailableResponse> checkUsernameAvailability(@PathVariable("username") String username) {
        return ResponseEntity.ok(authService.checkUsernameAvailability(username));
    }
}
