package com.twentyone.steachserver.domain.lectureStudents.model;

import com.twentyone.steachserver.domain.studentsQuizzes.model.StudentsQuizzesId;
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
public class LecturesStudentsId {
    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "lecture_id")
    private Integer lectureId;

    public static LecturesStudentsId createLecturesStudentsId(Integer studentId, Integer lectureId) {
        LecturesStudentsId LecturesStudentsId = new LecturesStudentsId();
        LecturesStudentsId.studentId = studentId;
        LecturesStudentsId.lectureId = lectureId;
        return LecturesStudentsId;
    }
}
