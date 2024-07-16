package com.twentyone.steachserver.domain.lectureStudent.model;

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
public class LectureStudentId {
    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "lecture_id")
    private Integer lectureId;

    public static LectureStudentId createLectureStudentId(Integer studentId, Integer lectureId) {
        LectureStudentId LectureStudentId = new LectureStudentId();
        LectureStudentId.studentId = studentId;
        LectureStudentId.lectureId = lectureId;
        return LectureStudentId;
    }
}
