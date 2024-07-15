package com.twentyone.steachserver.domain.lectureStudents.model;

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
@Table(
        name = "lectures_students",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lecture_student",
                        columnNames = {"students_id", "lectures_id"}
                )
        }
)
public class LecturesStudents {
    @EmbeddedId
    private LecturesStudentsId id;

    @ManyToOne
    @MapsId("studentId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "students_id")
    private Student student;

    @ManyToOne
    @MapsId("lectureId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "lectures_id")
    private Lecture lecture;

    @Column(name = "focus_ratio", precision = 4, scale = 2)
    private BigDecimal focusRatio = BigDecimal.ZERO;

    @Column(name = "focus_time", nullable = false)
    private Integer focusTime;

    protected LecturesStudents() {}

    private LecturesStudents(Student student, Lecture lecture) {
        this.id = LecturesStudentsId.createLecturesStudentsId(student.getId(), lecture.getId());
        this.student = student;
        this.lecture = lecture;
    }
}
