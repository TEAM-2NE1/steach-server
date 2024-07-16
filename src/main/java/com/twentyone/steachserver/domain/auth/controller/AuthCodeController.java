package com.twentyone.steachserver.domain.auth.controller;

import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateResponse;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeResponse;
import com.twentyone.steachserver.domain.auth.service.AuthCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/authcode")
@RequiredArgsConstructor
@RestController
public class AuthCodeController {
    private final AuthCodeService authCodeService;

    @PostMapping
    public ResponseEntity<AuthCodeResponse> create(@RequestBody final AuthCodeRequest authCodeRequest) {
        return ResponseEntity.ok(authCodeService.createNumberOfAuthCode(authCodeRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthCodeAuthenticateResponse> authenticate(@RequestBody final AuthCodeAuthenticateRequest request) {
        return ResponseEntity.ok(authCodeService.authenticate(request));
    }
}
