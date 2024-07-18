package com.twentyone.steachserver.domain.lecture;

import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureBeforeStartingResponseDto> getLectureInformation(@PathVariable Integer lectureId) {
        LectureBeforeStartingResponseDto lectureResponseDto = lectureService.getLectureInformation(lectureId);
        return ResponseEntity.ok().body(lectureResponseDto);
    }

    @GetMapping("/final/{lectureId}")
    public ResponseEntity<FinalLectureInfoByTeacherDto> getFinalLectureInformation(@PathVariable Integer lectureId) {
        lectureService.updateRealEndTime(lectureId);
        FinalLectureInfoByTeacherDto  finalLectureInfoByTeacherDto = lectureService.getFinalLectureInformation(lectureId);
        return ResponseEntity.ok().body(finalLectureInfoByTeacherDto);
    }


    @PatchMapping("/{lectureId}")
    public ResponseEntity<LectureBeforeStartingResponseDto> updateLectureInformation(@PathVariable Integer lectureId,
                                                                                     @RequestBody UpdateLectureRequestDto updatelectureRequestDto) {
        return ResponseEntity.ok().body(lectureService.updateLectureInformation(lectureId, updatelectureRequestDto));
    }
}
