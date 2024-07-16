package com.twentyone.steachserver.domain.classroom;

import com.twentyone.steachserver.domain.classroom.dto.ClassroomResponseDto;
import com.twentyone.steachserver.domain.classroom.dto.FinalClassroomRequestDto;
import com.twentyone.steachserver.domain.classroom.dto.FocusTimeRequestDto;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRoomsResponseDto;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.model.UpComingClassRooms;
import com.twentyone.steachserver.domain.classroom.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<ClassroomResponseDto> confirmStudentByApply(@PathVariable Integer studentId,
                                                            @PathVariable Integer lectureId) {
        Optional<Classroom> classroomOptional = classroomService.findByStudentIdAndLectureId(studentId, lectureId);
        return classroomOptional
                .map(classroom -> ResponseEntity.ok().
                        body(ClassroomResponseDto.createClassroomResponseDto(classroom)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/final/{studentId}/{lectureId}")
    public ResponseEntity<?> saveFinalClassroomState(@PathVariable Integer studentId,
                                                          @PathVariable Integer lectureId,
                                                          @RequestBody FinalClassroomRequestDto finalClassroomRequestDto) {
        classroomService.saveFinalClassroomState(studentId, lectureId, finalClassroomRequestDto);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/focus-time/{studentId}/{lectureId}")
    public ResponseEntity<?> submitTimeFocusTime(@PathVariable Integer studentId,
                                                 @PathVariable Integer lectureId,
                                                 @RequestBody FocusTimeRequestDto focusTimeDto) {
        classroomService.saveTimeFocusTime(studentId, lectureId, focusTimeDto.getFocusTime());
        return ResponseEntity.ok().build();
    }


}
