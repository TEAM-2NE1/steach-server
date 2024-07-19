package com.twentyone.steachserver.domain.classroom.dto;

import com.twentyone.steachserver.domain.classroom.model.Classroom;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public record UpComingClassRooms(List<Classroom> classrooms) {
    public UpComingClassRooms {
        classrooms = new ArrayList<>(classrooms);
    }

    public static UpComingClassRooms createEmptyUpComingClassRooms() {
        return new UpComingClassRooms(new ArrayList<>());
    }

    public static UpComingClassRooms createUpComingClassRooms(List<Classroom> classrooms) {
        return new UpComingClassRooms(classrooms);
    }

    public void addClassroom(Classroom classroom) {
        classrooms.add(classroom);
    }
}
