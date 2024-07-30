package com.twentyone.steachserver.domain.auth.error;

public class AuthCodeAlreadyInUseException extends RuntimeException {
    public AuthCodeAlreadyInUseException() {
    }

    public AuthCodeAlreadyInUseException(String message) {
        super(message);
    }
}
