package com.twentyone.steachserver.domain.studentLecture.model;

import com.twentyone.steachserver.domain.common.BaseTimeEntity;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@Entity
@Table(name = "students_lectures")
public class StudentLecture extends BaseTimeEntity {
    @EmbeddedId
    private StudentLectureId id;

    @Column(name = "focus_ratio", precision = 5, scale = 2)
    private BigDecimal focusRatio = BigDecimal.valueOf(100.00);

    // minute
    @Column(name = "focus_time", columnDefinition = "SMALLINT(6)")
    private Integer focusTime = 0;

    @Column(name = "quiz_answer_count", columnDefinition = "SMALLINT(6)")
    private Integer quizAnswerCount = 0;

    @Column(name = "quiz_total_score", columnDefinition = "SMALLINT(6)")
    private Integer quizTotalScore = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("lectureId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    private Lecture lecture;

    protected StudentLecture() {
    }

    private StudentLecture(Student student, Lecture lecture) {
        this.id = StudentLectureId.createStudentLectureId(student.getId(), lecture.getId());
        this.student = student;
        this.lecture = lecture;
    }

    public static StudentLecture of(Student student, Lecture lecture) {
        StudentLecture studentLecture = new StudentLecture(student, lecture);
        student.addStudentLecture(studentLecture);
        lecture.addStudentLecture(studentLecture);

        return studentLecture;
    }

    public static StudentLecture of(Student student, Lecture lecture, Integer focusTime) {
        StudentLecture studentLecture = new StudentLecture(student, lecture);
        studentLecture.focusTime = focusTime;

        return studentLecture;
    }

    public void sumFocusTime(Integer focusTime) {
        this.focusTime += focusTime;
    }

    public void updateNewFocusTime(Integer focusTime) {
        this.focusTime = focusTime;
    }

    public void updateFocusRatio(BigDecimal focusRatio) {
        this.focusRatio = focusRatio;
    }

    public void updateQuizAnswerCount(Integer quizAnswerCount) {
        this.quizAnswerCount = quizAnswerCount;
    }

    public void updateQuizTotalScore(Integer quizTotalScore) {
        this.quizTotalScore = quizTotalScore;
    }
}
