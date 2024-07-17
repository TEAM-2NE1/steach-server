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
@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("STUDENT") // 엔티티 타입 식별자 값 지정 / Student 엔티티가 type 컬럼에 저장할 값을 지정합니다.
@PrimaryKeyJoinColumn(name = "login_credential_id") // 상속받은 엔티티의 기본 키를 지정
public class Student extends LoginCredential{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    private String email = "";

//    @OneToOne
//    @JoinColumn(name = "login_credential_id", referencedColumnName = "id", nullable = false)
//    private LoginCredential loginCredential;

    @OneToMany(mappedBy = "student")
    private List<StudentQuiz> studentQuiz = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StudentCurriculum> studentsCurricula = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<LectureStudent> lectureStudents = new ArrayList<>();

    public static Student of(LoginCredential loginCredential, String name) {
        Student student = new Student();
        student.name = name;
//        student.loginCredential = loginCredential;
        LoginCredential studentLoginCredential = student;
        studentLoginCredential.of(loginCredential);
        return student;
    }
}