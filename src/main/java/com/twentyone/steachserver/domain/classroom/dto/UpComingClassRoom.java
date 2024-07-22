package com.twentyone.steachserver.domain.classroom.dto;

public record UpComingClassRoom(String sessionId){
    public static UpComingClassRoom of(String sessionId) {
        return new UpComingClassRoom(sessionId);
    }
}
