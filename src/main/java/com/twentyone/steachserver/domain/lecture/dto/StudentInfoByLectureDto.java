package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizByLectureDto;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class StudentInfoByLectureDto {
    private final String studentName;

    List<StudentQuizByLectureDto> studentQuizByLectureDtos;

    private final BigDecimal focusRatio; // 수업시간 - 졸은 시간 / 100
    private final Integer focusMinute; // minute
    private Integer totalQuizScore = 0;
    private Integer correctNumber = 0;

    private StudentInfoByLectureDto(List<StudentQuizByLectureDto> studentQuizByLectureDtos, BigDecimal focusRatio, Integer focusMinute, String studentName) {
        this.studentQuizByLectureDtos = studentQuizByLectureDtos;
        for (StudentQuizByLectureDto studentQuizDto : studentQuizByLectureDtos) {
            this.totalQuizScore += studentQuizDto.score();
            this.correctNumber += studentQuizDto.score() == 0 ? 0 : 1;
        }
        this.focusRatio = focusRatio;
        this.focusMinute = focusMinute;
        this.studentName = studentName;
    }

    public static StudentInfoByLectureDto of(List<StudentQuizByLectureDto> studentQuizByLectureDto, StudentLecture studentLecture) {
        return new StudentInfoByLectureDto(studentQuizByLectureDto, studentLecture.getFocusRatio(), studentLecture.getFocusTime(), studentLecture.getStudent().getName());
    }
}
