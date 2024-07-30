package com.twentyone.steachserver.domain.lecture.error;

public class LectureTimeNotYetException extends RuntimeException {
    public LectureTimeNotYetException() {
    }

    public LectureTimeNotYetException(String message) {
        super(message);
    }
}
