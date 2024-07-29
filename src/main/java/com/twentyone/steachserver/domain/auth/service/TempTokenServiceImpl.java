package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempTokenServiceImpl implements TempTokenService {
    private final JwtService jwtService;

    @Override
    public void validateToken(String token, LoginCredential credential) {
        if (!jwtService.isTempTokenValid(token, credential)) {
            throw new ForbiddenException("토큰이 유효하지 않음");
        }
    }
}
