package com.twentyone.steachserver.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DUPLICATE_REGISTRATION_NOT_ALLOWED(HttpStatus.CONFLICT, "중복수강신청이 불가능합니다."),
    LECTURE_TIME_NOT_YET(HttpStatus.FORBIDDEN, "아직 강의 시간이 아닙니다."),
    INSUFFICIENT_PRIVILEGES(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    STUDENT_PRIVILEGES_REQUIRED(HttpStatus.FORBIDDEN, "학생 권한만 가능합니다."),
    TEACHER_PRIVILEGES_REQUIRED(HttpStatus.FORBIDDEN, "강사 권한만 가능합니다."),
    AUTH_CODE_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "이미 사용된 auth code입니다.");

    private HttpStatus status;
    private String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
