package com.twentyone.steachserver.domain.studentLecture.controller;

import com.twentyone.steachserver.domain.classroom.dto.FocusTimeRequestDto;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/studentsLectures")
@RequiredArgsConstructor
public class StudentLectureController {

    private final StudentLectureService studentLectureService;

    @Operation(summary = "학생의 집중도를 받아서 저장 ", description = "무조건 200을 반환")
    @PostMapping("/focus-time/{lectureId}")
    public ResponseEntity<?> submitTimeFocusTime(@AuthenticationPrincipal Student student,
                                                 @PathVariable Integer lectureId,
                                                 @RequestBody FocusTimeRequestDto focusTimeDto) {
        studentLectureService.saveTimeFocusTime(student.getId(), lectureId, focusTimeDto.focusTime());
        return ResponseEntity.ok().build();
    }
}
