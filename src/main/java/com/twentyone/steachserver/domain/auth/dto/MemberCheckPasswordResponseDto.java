package com.twentyone.steachserver.domain.auth.dto;

public record MemberCheckPasswordResponseDto(String tempToken) {
    public static MemberCheckPasswordResponseDto of(String tempToken) {
        return new MemberCheckPasswordResponseDto(tempToken);
    }
}
