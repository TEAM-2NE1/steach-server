package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateResponse;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeResponse;

public interface AuthCodeService {
    AuthCodeResponse createNumberOfAuthCode(AuthCodeRequest authCodeRequest);

    AuthCodeAuthenticateResponse authenticate(AuthCodeAuthenticateRequest request);
}
