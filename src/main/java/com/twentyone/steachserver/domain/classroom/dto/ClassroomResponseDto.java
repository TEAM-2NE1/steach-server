package com.twentyone.steachserver.domain.classroom.dto;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import lombok.AccessLevel;
import lombok.Setter;

@Setter(value = AccessLevel.PRIVATE)
public class ClassroomResponseDto {
    private String sessionId;

    private ClassroomResponseDto() {}

    private ClassroomResponseDto(String sessionId) {
        this.sessionId = sessionId;
    }

    public static ClassroomResponseDto createClassroomResponseDto(Classroom classroom) {
        return new ClassroomResponseDto(classroom.getSessionId());
    }
}
