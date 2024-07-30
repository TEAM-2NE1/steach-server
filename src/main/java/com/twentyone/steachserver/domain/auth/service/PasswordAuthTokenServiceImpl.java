package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.error.UnAuthorizedException;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordAuthTokenServiceImpl implements PasswordAuthTokenService {
    private final JwtService jwtService;

    @Override
    public void validateToken(String passwordAuthToken, LoginCredential credential) {
        if (!jwtService.isPasswordAuthTokenValid(passwordAuthToken, credential)) {
            throw new UnAuthorizedException("토큰이 유효하지 않음");
        }
    }
}
