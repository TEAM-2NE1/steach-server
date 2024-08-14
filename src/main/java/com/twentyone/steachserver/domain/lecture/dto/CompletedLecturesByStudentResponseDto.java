package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CompletedLecturesByStudentResponseDto(LocalDateTime realStartTime,
                                                    LocalDateTime realEndTime,
                                                    Integer quizNumber,
                                                    StudentInfoByLectureDto studentsQuizzesByLectureDto) {
    public static CompletedLecturesByStudentResponseDto of(Lecture lecture, StudentLecture studentLecture, Integer quizNumber) {
        Integer focusTime = studentLecture.getFocusTime();
        BigDecimal focusRatio = studentLecture.getFocusRatio();
        Integer quizAnswerCount = studentLecture.getQuizAnswerCount();
        Integer quizTotalScore = studentLecture.getQuizTotalScore();
        StudentInfoByLectureDto studentInfoByLectureDto = StudentInfoByLectureDto.of(studentLecture);
        return new CompletedLecturesByStudentResponseDto(lecture.getRealStartTime(), lecture.getRealEndTime(), quizNumber, studentInfoByLectureDto);
    }
}
