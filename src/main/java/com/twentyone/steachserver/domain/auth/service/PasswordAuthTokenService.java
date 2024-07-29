package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;

public interface PasswordAuthTokenService {
    void validateToken(String token, LoginCredential credential);
}
