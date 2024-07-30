package com.twentyone.steachserver.domain.studentCurriculum.model;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.persistence.*;
import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "students_curricula")
public class StudentCurriculum {
    @EmbeddedId
    private StudentCurriculumId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("curriculumId")
    @JoinColumn(name = "curriculum_id", referencedColumnName = "id")
    private Curriculum curriculum;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    protected StudentCurriculum() {}

    public StudentCurriculum(Student student, Curriculum curriculum) {
        this.id = StudentCurriculumId.createStudentCurriculumId(student.getId(), curriculum.getId());
        this.student = student;
        this.curriculum = curriculum;

        student.addCurriculum(this);
        curriculum.addStudentCurriculum(this);
    }
}
