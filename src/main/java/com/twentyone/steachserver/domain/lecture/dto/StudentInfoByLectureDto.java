package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class StudentInfoByLectureDto {
    List<StudentQuizDto> StudentQuizzesDto;
    private final BigDecimal focusRatio; // 수업시간 - 졸은 시간 / 100
    private final Integer focusMinute; // minute
    private Integer totalQuizScore;
    private Integer correctNumber;

    public StudentInfoByLectureDto(List<StudentQuizDto> StudentQuizzesDto, BigDecimal focusRatio, Integer focusMinute) {
        this.StudentQuizzesDto = StudentQuizzesDto;
        for (StudentQuizDto studentQuizDto : StudentQuizzesDto) {
            this.totalQuizScore += studentQuizDto.score();
            this.correctNumber += studentQuizDto.score() == 0 ? 0 : 1;
        }
        this.focusRatio = focusRatio;
        this.focusMinute = focusMinute;
    }

    public static StudentInfoByLectureDto of(List<StudentQuizDto> StudentQuizzesDto, StudentLecture studentLecture) {
        return new StudentInfoByLectureDto(StudentQuizzesDto, studentLecture.getFocusRatio(), studentLecture.getFocusTime());
    }
}
