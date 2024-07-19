package com.twentyone.steachserver.domain.classroom.dto;

import lombok.AccessLevel;
import lombok.Setter;

public record UpComingClassRoomsResponseDto(UpComingClassRooms upComingClassRooms) {
    public static UpComingClassRoomsResponseDto createUpComingClassRoomsResponseDto(UpComingClassRooms upComingClassRooms) {
        return new UpComingClassRoomsResponseDto(upComingClassRooms);
    }
}
