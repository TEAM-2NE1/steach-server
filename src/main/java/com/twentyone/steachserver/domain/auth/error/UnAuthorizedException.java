package com.twentyone.steachserver.domain.auth.error;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException() {
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
