package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class UpdateLectureRequestDto {
    private String lectureOrder;
    private String title;
    private LocalDateTime lectureStartTime;
}
