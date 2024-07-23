package com.twentyone.steachserver;

import com.twentyone.steachserver.domain.curriculum.error.DuplicatedCurriculumRegistrationException;
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
    public ResponseEntity handleDuplicatedCurriculumRegistrationException(DuplicatedCurriculumRegistrationException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("중복 수강신청은 불가능합니다.");
    }
}
