package com.twentyone.steachserver.domain.studentCurricula.model;

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
public class StudentsCurriculaId {

    @Column(name = "student_id")
    private Integer studentId;
    @Column(name = "curricula_id")
    private Integer curriculaId;

    public static StudentsCurriculaId createStudentsCurriculaId(Integer studentId, Integer curriculaId) {
        StudentsCurriculaId studentsCurriculaId = new StudentsCurriculaId();
        studentsCurriculaId.studentId = studentId;
        studentsCurriculaId.curriculaId = curriculaId;
        return studentsCurriculaId;
    }
}
