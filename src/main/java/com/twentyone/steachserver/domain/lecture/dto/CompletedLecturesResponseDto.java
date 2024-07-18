package com.twentyone.steachserver.domain.lecture.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompletedLecturesResponseDto {
    private Integer lectureOrder;
    private String curriculumTitle;
    private String lectureTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // 참여 학생들의 정보
    private List<StudentInfoByLectureDto> quizzes = new ArrayList<>();
}
