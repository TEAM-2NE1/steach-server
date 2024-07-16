package com.twentyone.steachserver.domain.classroom.service;

import com.twentyone.steachserver.domain.classroom.dto.FinalClassroomRequestDto;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.model.UpComingClassRooms;

import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    UpComingClassRooms upcomingClassroom();
    void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime);
    void saveFinalClassroomState(Integer studentId, Integer lectureId, FinalClassroomRequestDto finalClassroomRequestDto);
    Optional<Classroom> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);
}
