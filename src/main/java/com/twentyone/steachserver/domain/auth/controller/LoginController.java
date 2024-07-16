package com.twentyone.steachserver.domain.auth.controller;

import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.SignUpDto;
import com.twentyone.steachserver.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final AuthService authService;

    @PostMapping("/student/join")
    public ResponseEntity signup(@RequestBody SignUpDto signupDto) {
        authService.signUpStudent(signupDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        String accessToken = authService.login(loginDto);

        return ResponseEntity.ok(accessToken);
    }
}
