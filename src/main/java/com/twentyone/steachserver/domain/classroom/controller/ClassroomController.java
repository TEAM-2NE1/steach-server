package com.twentyone.steachserver.domain.classroom.controller;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.classroom.dto.ClassroomResponseDto;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRoomsResponseDto;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRooms;
import com.twentyone.steachserver.domain.classroom.service.ClassroomService;
import com.twentyone.steachserver.domain.member.model.Student;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;


    @Operation(summary = "전체 강의에서 다가오는 강의들을 반환 ", description = "무조건 200을 반환")
    @GetMapping("/upcoming")
    public ResponseEntity<?> upcoming() {
        UpComingClassRooms classrooms = classroomService.upcomingClassroom();

        UpComingClassRoomsResponseDto upComingClassRoomsResponseDto = UpComingClassRoomsResponseDto.
                createUpComingClassRoomsResponseDto(classrooms);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(upComingClassRoomsResponseDto);
    }

    @Operation(summary = "강의를 들을 학생인지 확인 ", description = "권한이 있으면 200을 반환, 없으면 403")
    @GetMapping("/{lectureId}")
    public ResponseEntity<ClassroomResponseDto> confirmStudentByApply(@AuthenticationPrincipal Student student, @PathVariable("lectureId") Integer lectureId) {
        Optional<Classroom> classroomOptional = classroomService.getClassroomByLectureAndStudent(student.getId(), lectureId);
        return classroomOptional
                .map(classroom -> ResponseEntity.ok().
                        body(ClassroomResponseDto.createClassroomResponseDto(classroom)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
