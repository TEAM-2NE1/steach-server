package com.twentyone.steachserver.interfaces.advice;

import com.twentyone.steachserver.domain.auth.error.AuthCodeAlreadyInUseException;
import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.auth.error.UnAuthorizedException;
import com.twentyone.steachserver.domain.curriculum.error.DuplicatedCurriculumRegistrationException;
import com.twentyone.steachserver.domain.lecture.error.LectureTimeNotYetException;
import com.twentyone.steachserver.global.error.ErrorCode;
import com.twentyone.steachserver.global.error.ErrorResponseDto;
import com.twentyone.steachserver.global.error.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[잘못된 입력] " + e.getMessage());
    }

    @ExceptionHandler(DuplicatedCurriculumRegistrationException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicatedCurriculumRegistrationException(DuplicatedCurriculumRegistrationException e) {
        log.info(e.getMessage());
        return getResponse(ErrorCode.DUPLICATE_REGISTRATION_NOT_ALLOWED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbiddenException(ForbiddenException e) {
        log.info(e.getMessage());
        return getResponse(ErrorCode.INSUFFICIENT_PRIVILEGES);
    }

    @ExceptionHandler(LectureTimeNotYetException.class)
    public ResponseEntity<ErrorResponseDto> handleLectureTimeNotYetException(LectureTimeNotYetException e) {
        log.info(e.getMessage());
        return getResponse(ErrorCode.LECTURE_TIME_NOT_YET);
    }

    @ExceptionHandler(AuthCodeAlreadyInUseException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthCodeAlreadyInUseException(AuthCodeAlreadyInUseException e) {
        log.info(e.getMessage());
        return getResponse(ErrorCode.AUTH_CODE_ALREADY_IN_USE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String> handleUnAuthorizedException(UnAuthorizedException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private static ResponseEntity<ErrorResponseDto> getResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponseDto(errorCode));
    }
}
