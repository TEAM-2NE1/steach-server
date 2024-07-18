package com.twentyone.steachserver.domain.studentLecture;

import com.twentyone.steachserver.domain.classroom.dto.FocusTimeRequestDto;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/studentsLectures")
@RequiredArgsConstructor
public class StudentLectureController {

    private final StudentLectureService studentLectureService;

    @PostMapping("/focus-time/{studentId}/{lectureId}")
    public ResponseEntity<?> submitTimeFocusTime(@PathVariable Integer studentId,
                                                 @PathVariable Integer lectureId,
                                                 @RequestBody FocusTimeRequestDto focusTimeDto) {
        studentLectureService.saveTimeFocusTime(studentId, lectureId, focusTimeDto.focusTime());
        return ResponseEntity.ok().build();
    }
}
