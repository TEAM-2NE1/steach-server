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
    private Integer curriculumId;

    public static StudentCurriculumId createStudentCurriculumId(Integer studentId, Integer curriculumId) {
        StudentCurriculumId studentCurriculumId = new StudentCurriculumId();
        studentCurriculumId.studentId = studentId;
        studentCurriculumId.curriculumId = curriculumId;
        return studentCurriculumId;
    }
}
