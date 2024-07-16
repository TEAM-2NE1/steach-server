package com.twentyone.steachserver.domain.classroom.dto;

import lombok.AccessLevel;
import lombok.Setter;

@Setter(value = AccessLevel.PRIVATE)
public class UpComingClassRoomsResponseDto {
    private UpComingClassRooms upComingClassRooms;

    private UpComingClassRoomsResponseDto(UpComingClassRooms upComingClassRooms) {
        this.upComingClassRooms = upComingClassRooms;
    }

    public static UpComingClassRoomsResponseDto createUpComingClassRoomsResponseDto(UpComingClassRooms upComingClassRooms) {
        return new UpComingClassRoomsResponseDto(upComingClassRooms);
    }
}
