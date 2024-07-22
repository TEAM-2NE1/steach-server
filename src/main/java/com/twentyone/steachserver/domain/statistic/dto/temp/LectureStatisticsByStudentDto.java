package com.twentyone.steachserver.domain.statistic.dto.temp;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import java.math.BigDecimal;

public record LectureStatisticsByStudentDto(
        Integer studentId,
        String studentName,
        Integer lectureId,
        Integer quizCount,
        Integer totalQuizTotalScore,
        Integer totalQuizAnswerCount,
        Integer totalFocusTime,
        BigDecimal totalFocusRatio) {

    public LectureStatisticsByStudentDto(Student student, Lecture lecture) {
        this(
                student.getId(),
                student.getName(),
                lecture.getId(),
                0,
                0,
                0,
                0,
                BigDecimal.ZERO
        );
    }

    public static LectureStatisticsByStudentDto of(Student student, Lecture lecture) {
        return new LectureStatisticsByStudentDto(student, lecture);
    }

    public static LectureStatisticsByStudentDto of(Student student, Lecture lecture, Integer totalQuizTotalScore, Integer totalQuizAnswerCount, Integer totalFocusTime, BigDecimal totalFocusRatio) {
        return new LectureStatisticsByStudentDto(
                student.getId(),
                student.getName(),
                lecture.getId(),
                0,
                totalQuizTotalScore,
                totalQuizAnswerCount,
                totalFocusTime,
                totalFocusRatio
        );
    }

    public LectureStatisticsByStudentDto addLectureData(StudentLecture studentLecture) {
        return new LectureStatisticsByStudentDto(
                this.studentId,
                this.studentName,
                this.lectureId,
                this.quizCount + 1,
                this.totalQuizTotalScore + studentLecture.getQuizTotalScore(),
                this.totalQuizAnswerCount + studentLecture.getQuizAnswerCount(),
                this.totalFocusTime + studentLecture.getFocusTime(),
                this.totalFocusRatio.add(studentLecture.getFocusRatio())
        );
    }
}
