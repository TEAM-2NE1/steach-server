package com.twentyone.steachserver.domain.curriculum.error;

public class DuplicatedCurriculumRegistrationException extends RuntimeException {
    public DuplicatedCurriculumRegistrationException() {
    }

    public DuplicatedCurriculumRegistrationException(String message) {
        super(message);
    }
}
