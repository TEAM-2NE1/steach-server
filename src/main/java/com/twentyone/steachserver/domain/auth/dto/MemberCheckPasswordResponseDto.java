package com.twentyone.steachserver.domain.auth.dto;

public record MemberCheckPasswordResponseDto(String passwordAuthToken) {
    public static MemberCheckPasswordResponseDto of(String passwordAuthToken) {
        return new MemberCheckPasswordResponseDto(passwordAuthToken);
    }
}
