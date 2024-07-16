package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeAuthenticateResponse;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeRequest;
import com.twentyone.steachserver.domain.auth.dto.AuthCodeResponse;
import com.twentyone.steachserver.domain.auth.model.AuthCode;
import com.twentyone.steachserver.domain.auth.repository.AuthCodeRepository;
import com.twentyone.steachserver.domain.auth.util.RandomStringGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthCodeServiceImpl implements AuthCodeService {
    private final AuthCodeRepository authCodeRepository;

    @Override
    @Transactional
    public AuthCodeResponse createNumberOfAuthCode(AuthCodeRequest authCodeRequest) {
        List<String> authCodes = new ArrayList<>(authCodeRequest.getNumberOfCode());

        for (int i = 0; i < authCodeRequest.getNumberOfCode(); i++) {
            try {
                String createdWord = RandomStringGenerator.getNew();
                AuthCode created = AuthCode.of(createdWord);
                authCodeRepository.save(created);
                authCodes.add(createdWord);
            } catch (Exception e) {
                i--;
            }
        }

        return AuthCodeResponse.builder()
                .authCode(authCodes)
                .build();
    }

    @Override
    @Transactional
    public AuthCodeAuthenticateResponse authenticate(AuthCodeAuthenticateRequest request) {
        Boolean isValid;

        try {
            validAuthCode(request.getAuthCode());
            isValid = true;
        } catch (RuntimeException e) {
            isValid = false;
        }

        return AuthCodeAuthenticateResponse.builder()
                .authentication(isValid)
                .build();
    }

    private void validAuthCode(String authCodeWord) {
        if (authCodeWord.length() != 30) {
            throw new RuntimeException("유효하지 않은 코드");
        }

        AuthCode authCode = authCodeRepository.findById(authCodeWord)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 코드"));

        if (authCode.getIsRegistered()) {
            throw new RuntimeException("이미 등록된 코드");
        }
    }
}
