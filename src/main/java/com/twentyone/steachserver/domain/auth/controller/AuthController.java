package com.twentyone.steachserver.domain.auth.controller;

import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.LoginResponseDto;
import com.twentyone.steachserver.domain.auth.dto.SignUpDto;
import com.twentyone.steachserver.domain.auth.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/student/join")
    public ResponseEntity signup(@RequestBody SignUpDto signupDto) {
        authServiceImpl.signUpStudent(signupDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/teacher/join")
    public ResponseEntity signupTeacher(@RequestBody SignUpDto signupDto) {
        authServiceImpl.signUpTeacher(signupDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authServiceImpl.login(loginDto));
    }
}
