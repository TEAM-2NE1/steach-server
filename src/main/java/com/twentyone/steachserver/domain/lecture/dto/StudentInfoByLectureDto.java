package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import lombok.Getter;

import java.util.List;

@Getter
public class StudentInfoByLectureDto {
    List<StudentQuizDto> StudentQuizzesDto;
    private final Integer focusRatio; // 수업시간 - 졸은 시간 / 100
    private final Integer sleepMinute; // minute
    private Integer totalQuizScore;
    private Integer correctNumber;

    public StudentInfoByLectureDto(List<StudentQuizDto> StudentQuizzesDto, Integer focusRatio, Integer sleepMinute) {
        this.StudentQuizzesDto = StudentQuizzesDto;
        for (StudentQuizDto studentQuizDto : StudentQuizzesDto) {
            this.totalQuizScore += studentQuizDto.getScore();
            this.correctNumber += studentQuizDto.getScore() == 0 ? 0 : 1;
        }
        this.focusRatio = focusRatio;
        this.sleepMinute = sleepMinute;
    }
}
