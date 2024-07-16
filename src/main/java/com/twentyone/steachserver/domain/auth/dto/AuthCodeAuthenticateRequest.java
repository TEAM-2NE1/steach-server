package com.twentyone.steachserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeAuthenticateRequest {
    private String authCode;
}
