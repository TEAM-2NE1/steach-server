package com.twentyone.steachserver.global.error;

import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private ErrorCode errorCode;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
