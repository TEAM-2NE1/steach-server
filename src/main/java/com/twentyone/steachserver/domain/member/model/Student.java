package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.lectureStudents.model.LecturesStudents;
import com.twentyone.steachserver.domain.member.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.studentCurricula.model.StudentsCurricula;
import com.twentyone.steachserver.domain.studentsQuizzes.model.StudentsQuizzes;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(length = 30)
    private String name;

    @OneToOne
    @JoinColumn(name = "login_credentials_id")
    private LoginCredential loginCredential;

    @OneToMany(mappedBy = "student")
    private Set<StudentsQuizzes> studentsQuizzes = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentsCurricula> studentsCurricula = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<LecturesStudents> lecturesStudents = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
