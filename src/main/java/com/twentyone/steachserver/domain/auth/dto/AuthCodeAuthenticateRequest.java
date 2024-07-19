package com.twentyone.steachserver.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeAuthenticateRequest {
    @JsonProperty("auth_code")
    private String authCode;
}
