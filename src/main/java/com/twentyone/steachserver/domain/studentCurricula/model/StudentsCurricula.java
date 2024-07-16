package com.twentyone.steachserver.domain.studentCurricula.model;

import com.twentyone.steachserver.domain.curricula.model.Curricula;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.persistence.*;
import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "students_curricula")
public class StudentsCurricula {
    @EmbeddedId
    private StudentsCurriculaId id;

    @ManyToOne
    @MapsId("curriculaId")
    @JoinColumn(name = "curriculum_id", referencedColumnName = "id")
    private Curricula curricula;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    protected StudentsCurricula() {}

    private StudentsCurricula(Student student, Curricula curricula) {
        this.id = StudentsCurriculaId.createStudentsCurriculaId(student.getId(), curricula.getId());
        this.student = student;
        this.curricula = curricula;
    }
}
