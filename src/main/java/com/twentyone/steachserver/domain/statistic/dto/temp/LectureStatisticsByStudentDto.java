package com.twentyone.steachserver.domain.statistic.dto.temp;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import jakarta.persistence.Id;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
public class LectureStatisticsByStudentDto {
    private final Integer studentId;
    private final String studentName;
    private final Integer lectureId;
    private Integer quizCount = 0;
    private Integer totalQuizTotalScore = 0;
    private Integer totalQuizAnswerCount = 0;
    private Integer totalFocusTime = 0;
    private BigDecimal totalFocusRatio = BigDecimal.ZERO;


    private LectureStatisticsByStudentDto(Student student, Lecture lecture) {
        this.studentId = student.getId();
        this.studentName = student.getName();
        this.lectureId = lecture.getId();
    }

    private LectureStatisticsByStudentDto(Student student, Lecture lecture, Integer totalQuizTotalScore, Integer totalQuizAnswerCount, Integer totalFocusTime, BigDecimal totalFocusRatio) {
        this.studentId = student.getId();
        this.studentName = student.getName();
        this.lectureId = lecture.getId();
        this.totalQuizTotalScore = totalQuizTotalScore;
        this.totalQuizAnswerCount = totalQuizAnswerCount;
        this.totalFocusTime = totalFocusTime;
        this.totalFocusRatio = totalFocusRatio;
    }

    public static LectureStatisticsByStudentDto of(Student student, Lecture lecture, Integer totalQuizTotalScore, Integer totalQuizAnswerCount, Integer totalFocusTime, BigDecimal totalFocusRatio) {
        return new LectureStatisticsByStudentDto(student, lecture, totalQuizTotalScore, totalQuizAnswerCount, totalFocusTime, totalFocusRatio);
    }

    public static LectureStatisticsByStudentDto of(Student student, Lecture lecture) {
        return new LectureStatisticsByStudentDto(student, lecture);
    }

    public void addLectureData(StudentLecture studentLecture) {
        this.quizCount++;
        this.totalFocusTime += studentLecture.getFocusTime();
        this.totalFocusRatio = totalFocusRatio.add(studentLecture.getFocusRatio());
        this.totalQuizAnswerCount += studentLecture.getQuizAnswerCount();
        this.totalQuizTotalScore += studentLecture.getQuizTotalScore();
    }
}
