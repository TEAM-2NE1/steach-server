package com.twentyone.steachserver.domain.studentCurriculum.model;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.persistence.*;
import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "students_curricula")
public class StudentCurriculum {
    @EmbeddedId
    private StudentCurriculumId id;

    @ManyToOne
    @MapsId("curriculaId")
    @JoinColumn(name = "curriculum_id", referencedColumnName = "id")
    private Curriculum curriculum;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    protected StudentCurriculum() {}

    private StudentCurriculum(Student student, Curriculum curriculum) {
        this.id = StudentCurriculumId.createStudentsCurriculaId(student.getId(), curriculum.getId());
        this.student = student;
        this.curriculum = curriculum;
    }
}
