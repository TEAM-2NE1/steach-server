package com.twentyone.steachserver.domain.lectureStudent.model;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "lectures_students")
public class LectureStudent {
    @EmbeddedId
    private LectureStudentId id;

    @ManyToOne
    @MapsId("studentId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @MapsId("lectureId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    private Lecture lecture;

    @Column(name = "focus_ratio", precision = 5, scale = 2)
    private BigDecimal focusRatio = BigDecimal.ZERO;

    @Column(name = "focus_time", nullable = false)
    private Integer focusTime;

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
