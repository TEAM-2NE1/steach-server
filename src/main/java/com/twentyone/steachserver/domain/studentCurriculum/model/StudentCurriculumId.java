package com.twentyone.steachserver.domain.studentCurriculum.model;

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
public class StudentCurriculumId {

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "curricula_id")
    private Integer curriculaId;

    public static StudentCurriculumId createStudentsCurriculaId(Integer studentId, Integer curriculaId) {
        StudentCurriculumId studentsCurriculaId = new StudentCurriculumId();
        studentsCurriculaId.studentId = studentId;
        studentsCurriculaId.curriculaId = curriculaId;
        return studentsCurriculaId;
    }
}
