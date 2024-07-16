package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.lectureStudents.model.LecturesStudents;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentsQuizzes.model.StudentsQuizzes;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "students")
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "login_credential_id", referencedColumnName = "id", nullable = false)
    private LoginCredential loginCredential;

    @OneToMany(mappedBy = "student")
    private List<StudentsQuizzes> studentsQuizzes = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StudentCurriculum> studentsCurricula = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<LecturesStudents> lecturesStudents = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}