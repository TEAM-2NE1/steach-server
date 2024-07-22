package com.twentyone.steachserver.domain.auth.controller;

import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateResponse;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeResponse;
import com.twentyone.steachserver.domain.auth.service.AuthCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "AuthCode", description = "AuthCode 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth-codes")
public class AuthCodeController {
    private final AuthCodeService authCodeService;

    @Operation(summary = "auth코드 발급", description = "201: 정상(1이상의 값)/ 400: 0이하의 값")
    @PostMapping
    public ResponseEntity<AuthCodeResponse> create(@RequestBody @Valid final AuthCodeRequest authCodeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authCodeService.createNumberOfAuthCode(authCodeRequest));
    }

    @Operation(summary = "auth코드 유효성 검사", description = "무조건 200을 반환, 유효여부는 true false로 반환")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthCodeAuthenticateResponse> authenticate(
            @RequestBody final AuthCodeAuthenticateRequest request) {
        return ResponseEntity.ok(authCodeService.authenticate(request));
    }
}
