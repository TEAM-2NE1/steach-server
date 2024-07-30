package com.twentyone.steachserver.domain.auth.dto;

import lombok.Builder;

@Builder
public record MemberCheckPasswordRequestDto(String password) {
}
