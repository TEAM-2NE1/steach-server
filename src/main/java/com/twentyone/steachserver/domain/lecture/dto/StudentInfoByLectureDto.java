package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.quiz.dto.QuizByLectureDto;

import java.util.List;

public class StudentInfoByLectureDto {
    List<QuizByLectureDto> quizByLectureDtoList;
    private Integer FocusRatio; // 수업시간 - 졸은 시간 / 100
    private Integer SleepTime; // minute
    private Integer totalQuizScore;
    private Integer correctNumber;
}
