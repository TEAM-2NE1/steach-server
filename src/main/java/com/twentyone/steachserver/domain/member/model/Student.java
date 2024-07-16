package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
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
@AllArgsConstructor
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
    private List<StudentQuiz> studentQuiz = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StudentCurriculum> studentsCurricula = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<LectureStudent> lectureStudent = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static Student of(LoginCredential loginCredential, String name) {
        Student student = new Student();
        student.name = name;
        student.loginCredential = loginCredential;

        return student;
    }
}