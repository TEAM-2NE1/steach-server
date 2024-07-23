package com.twentyone.steachserver.domain.classroom.controller;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.classroom.dto.ClassroomResponseDto;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRoomsResponseDto;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRooms;
import com.twentyone.steachserver.domain.classroom.service.ClassroomService;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "클래스룸")
@RestController
@RequestMapping("/api/v1/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;
    private final StudentLectureService studentLectureService;

    @Operation(summary = "전체 강의에서 다가오는 강의들을 반환!", description = "무조건 200을 반환하며 classroom을 만들어줍니다.")
    @GetMapping("/upcoming")
    public ResponseEntity<?> upcoming() {
        UpComingClassRooms classrooms = classroomService.upcomingClassroom();

        UpComingClassRoomsResponseDto upComingClassRoomsResponseDto = UpComingClassRoomsResponseDto.
                createUpComingClassRoomsResponseDto(classrooms);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(upComingClassRoomsResponseDto);
    }


    @Operation(summary = "강의를 들을 학생인지 확인!", description = "권한이 있으면 200을 반환, 없으면 403")
    @GetMapping("/check/{sessionId}")
    public ResponseEntity<ClassroomResponseDto> confirmStudentByApply(@AuthenticationPrincipal Student student, @PathVariable("sessionId") String sessionId) {
        Optional<Classroom> classroomOptional = classroomService.getClassroomBySessionIdAndStudent(student.getId(), sessionId);
        return classroomOptional
                .map(classroom -> ResponseEntity.ok().
                        body(ClassroomResponseDto.createClassroomResponseDto(classroom)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @Secured("ROLE_TEACHER")
    @Operation(summary = "선생님이 강의 시작을 누르면 교실로 들어가는 메서드", description = "무조건 200을 반환")
    @PatchMapping("/start/{lectureId}")
    public ResponseEntity<?> startClassroom(@PathVariable("lectureId") Integer lectureId) {
        studentLectureService.createStudentLectureByLecture(lectureId);
        Classroom classroom = classroomService.createClassroom(lectureId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ClassroomResponseDto.createClassroomResponseDto(classroom));
    }


}
