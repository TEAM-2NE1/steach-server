package com.twentyone.steachserver.domain.auth.dto;

public record MemberCheckPasswordResponseDto(Boolean isCorrect) {
    public static MemberCheckPasswordResponseDto of(Boolean isCorrect) {
        return new MemberCheckPasswordResponseDto(isCorrect);
    }
}
