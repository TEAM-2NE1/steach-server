package com.twentyone.steachserver.domain.lecture.controller;

import com.twentyone.steachserver.domain.classroom.dto.ClassroomResponseDto;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.lecture.dto.CompletedLecturesResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.update.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.service.StatisticService;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "강의")
@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final StudentLectureService studentLectureService;
    private final StatisticService statisticService;

    @Operation(summary = "강의에 대한 다양한 정보 반환", description = "무조건 200을 반환, 강의에 대해서 시작 전 강의면 시작 전 형태로, 끝난 강의는 끝난형태로 반환.")
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureBeforeStartingResponseDto> getLectureInformation(@PathVariable("lectureId")Integer lectureId) {
        LectureBeforeStartingResponseDto lectureResponseDto = lectureService.getLectureInformation(lectureId);

        if (lectureResponseDto.getIsCompleted()){
            CompletedLecturesResponseDto completedLecturesResponseDto = (CompletedLecturesResponseDto) lectureResponseDto;
            return ResponseEntity.ok().body(completedLecturesResponseDto);
        }

        return ResponseEntity.ok().body(lectureResponseDto);
    }

    @Operation(summary = "선생님이 강의를 끝내고 관련 정보 처리 및 최종정보 반환 ", description = "무조건 200을 반환")
    @GetMapping("/final/{lectureId}")
    public ResponseEntity<FinalLectureInfoByTeacherDto> getFinalLectureInformation(@PathVariable("lectureId") Integer lectureId) {
        Lecture updateLecture = lectureService.updateRealEndTime(lectureId);
        studentLectureService.updateStudentLectureByFinishLecture(lectureId);
        statisticService.createStatisticsByFinalLecture(updateLecture);
        FinalLectureInfoByTeacherDto finalLectureInfoByTeacherDto = lectureService.getFinalLectureInformation(lectureId);
        return ResponseEntity.ok().body(finalLectureInfoByTeacherDto);
    }


    @Operation(summary = "강의 수정!", description = "성공시 200 반환, 실패시 204 NO_CONTENT 반환.")
    @PatchMapping("/{lectureId}")
    public ResponseEntity<?> updateLectureInformation(@PathVariable("lectureId") Integer lectureId, @RequestBody UpdateLectureRequestDto updatelectureRequestDto) {
        return lectureService.updateLectureInformation(lectureId, updatelectureRequestDto)
                .map(lectureBeforeStartingResponseDto ->  ResponseEntity.ok().body(lectureBeforeStartingResponseDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @Operation(summary = "강의를 들을 학생인지 확인!", description = "권한이 있으면 200을 반환, 없으면 403")
    @GetMapping("/check/{lectureId}")
    public ResponseEntity<ClassroomResponseDto> confirmStudentByApply(@AuthenticationPrincipal Student student, @PathVariable("lectureId") Integer lectureId) {
        Optional<Classroom> classroomOptional = lectureService.getClassroomByLectureAndStudent(student.getId(), lectureId);
        return classroomOptional
                .map(classroom -> ResponseEntity.ok().
                        body(ClassroomResponseDto.createClassroomResponseDto(classroom)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

}
