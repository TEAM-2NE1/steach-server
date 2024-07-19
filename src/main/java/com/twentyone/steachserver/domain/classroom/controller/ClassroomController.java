package com.twentyone.steachserver.domain.classroom.controller;

import com.twentyone.steachserver.domain.classroom.dto.ClassroomResponseDto;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRoomsResponseDto;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRooms;
import com.twentyone.steachserver.domain.classroom.service.ClassroomService;
import com.twentyone.steachserver.domain.studentQuiz.service.StudentQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;


    @GetMapping("/upcoming")
    public ResponseEntity<?> upcoming() {
        // 한 시간에 한번씩 수업 1시간~2시간 이전에 남은 걸 확인해서 그 강의를 보내줌
        UpComingClassRooms classrooms = classroomService.upcomingClassroom();

        UpComingClassRoomsResponseDto upComingClassRoomsResponseDto = UpComingClassRoomsResponseDto.
                createUpComingClassRoomsResponseDto(classrooms);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(upComingClassRoomsResponseDto);
    }

    @GetMapping("/{studentId}/{lectureId}")
    public ResponseEntity<ClassroomResponseDto> confirmStudentByApply(@PathVariable("studentId") Integer studentId, @PathVariable("lectureId") Integer lectureId) {
        Optional<Classroom> classroomOptional = classroomService.getClassroomByLectureAndStudent(studentId, lectureId);
        return classroomOptional
                .map(classroom -> ResponseEntity.ok().
                        body(ClassroomResponseDto.createClassroomResponseDto(classroom)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
