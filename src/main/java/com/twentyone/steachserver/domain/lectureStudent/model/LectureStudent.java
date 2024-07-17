package com.twentyone.steachserver.domain.lectureStudent.model;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)

@Entity
@Table(name = "lectures_students")
public class LectureStudent {
    @EmbeddedId
    private LectureStudentId id;

    @Column(name = "focus_ratio", precision = 5, scale = 2)
    private BigDecimal focusRatio = BigDecimal.ZERO;

    @Column(name = "focus_time")
    private Integer focusTime;

    @Column(name = "quiz_answer_count")
    private Integer quizAnswerCount = 0;

    @Column(name = "quiz_total_score")
    private Integer quizTotalScore = 0;

    @ManyToOne
    @MapsId("studentId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @MapsId("lectureId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    private Lecture lecture;

    protected LectureStudent() {}

    private LectureStudent(Student student, Lecture lecture) {
        this.id = LectureStudentId.createLectureStudentId(student.getId(), lecture.getId());
        this.student = student;
        this.lecture = lecture;
    }

    public static LectureStudent createLectureStudent(Student student, Lecture lecture, Integer focusTime) {
        LectureStudent lectureStudent = new LectureStudent(student, lecture);
        lectureStudent.focusTime = focusTime;
        return lectureStudent;
    }

    public void sumFocusTime(Integer focusTime) {
        this.focusTime += focusTime;
    }
}
