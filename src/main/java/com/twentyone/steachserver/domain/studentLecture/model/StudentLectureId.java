package com.twentyone.steachserver.domain.studentLecture.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class StudentLectureId {
    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "lecture_id")
    private Integer lectureId;

    public static StudentLectureId createStudentLectureId(Integer studentId, Integer lectureId) {
        StudentLectureId studentLectureId = new StudentLectureId();
        studentLectureId.studentId = studentId;
        studentLectureId.lectureId = lectureId;
        return studentLectureId;
    }
}
