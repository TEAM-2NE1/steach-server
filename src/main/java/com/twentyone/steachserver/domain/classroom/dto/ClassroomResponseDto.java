package com.twentyone.steachserver.domain.classroom.dto;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import lombok.AccessLevel;
import lombok.Setter;

public record ClassroomResponseDto(String sessionId) {
    public static ClassroomResponseDto createClassroomResponseDto(Classroom classroom) {
        return new ClassroomResponseDto(classroom.getSessionId());
    }
}
