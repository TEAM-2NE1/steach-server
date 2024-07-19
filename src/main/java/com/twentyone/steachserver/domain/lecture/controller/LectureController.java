package com.twentyone.steachserver.domain.lecture.controller;

import com.twentyone.steachserver.domain.lecture.dto.CompletedLecturesResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.studentLecture.dto.StudentLectureStatisticDto;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final StudentLectureService studentLectureService;

    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureBeforeStartingResponseDto> getLectureInformation(@PathVariable Integer lectureId) {
        LectureBeforeStartingResponseDto lectureResponseDto = lectureService.getLectureInformation(lectureId);

        if (lectureResponseDto.getIsCompleted()){
            CompletedLecturesResponseDto completedLecturesResponseDto = (CompletedLecturesResponseDto) lectureResponseDto;
            return ResponseEntity.ok().body(completedLecturesResponseDto);
        }

        return ResponseEntity.ok().body(lectureResponseDto);

    }

    // teacher
    @GetMapping("/final/{lectureId}")
    public ResponseEntity<FinalLectureInfoByTeacherDto> getFinalLectureInformation(@PathVariable Integer lectureId) {
        lectureService.updateRealEndTime(lectureId);
        studentLectureService.updateStudentLectureByFinishLecture(lectureId);
        StudentLectureStatisticDto lectureStudentStatistic = studentLectureService.createLectureStudentStatistic(lectureId);
        lectureService.createGPTData(lectureId, lectureStudentStatistic);
        FinalLectureInfoByTeacherDto finalLectureInfoByTeacherDto = lectureService.getFinalLectureInformation(lectureId);
        return ResponseEntity.ok().body(finalLectureInfoByTeacherDto);
    }


    @PatchMapping("/{lectureId}")
    public ResponseEntity<LectureBeforeStartingResponseDto> updateLectureInformation(@PathVariable Integer lectureId, @RequestBody UpdateLectureRequestDto updatelectureRequestDto) {
        return ResponseEntity.ok().body(lectureService.updateLectureInformation(lectureId, updatelectureRequestDto));
    }
}
