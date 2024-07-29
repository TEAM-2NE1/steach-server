package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;

public interface TempTokenService {
    void validateToken(String token, LoginCredential credential);
}
