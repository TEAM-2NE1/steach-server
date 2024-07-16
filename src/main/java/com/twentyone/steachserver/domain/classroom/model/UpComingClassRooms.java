package com.twentyone.steachserver.domain.classroom.model;

import lombok.AccessLevel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter(value = AccessLevel.PRIVATE)
public class UpComingClassRooms {
    private List<Classroom> classrooms = new ArrayList<>();

    private UpComingClassRooms() {}

    private UpComingClassRooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public static UpComingClassRooms createEmptyUpComingClassRooms() {
        return new UpComingClassRooms();
    }

    public static UpComingClassRooms createUpComingClassRooms(List<Classroom> classrooms) {
        return new UpComingClassRooms(classrooms);
    }

    public void addClassroom(Classroom classroom) {
        this.classrooms.add(classroom);
    }
}
