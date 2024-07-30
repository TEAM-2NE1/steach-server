package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명
 * 상위 클래스(LoginCredential): @Inheritance(strategy = InheritanceType.JOINED)를 사용하여 상속 전략을 정의합니다.
 * 하위 클래스(Student 및 Admin): @PrimaryKeyJoinColumn을 지정할 필요 없이 상위 클래스 LoginCredential에서 상속됩니다.
 *
 * @PrimaryKeyJoinColumn 사용법
 * 필요한 경우 @PrimaryKeyJoinColumn 주석을 사용하여 외래 키 매핑을 명시적으로 정의할 수 있지만 기본 JPA 동작이 요구 사항에 맞는 경우 필수는 아닙니다. JPA는 상위 클래스에 정의된 상속 전략에 따라 기본 키 조인을 자동으로 처리합니다.
 * <p>
 * 요약
 * 상위 클래스에 @Inheritance 주석만 있으면 상속이 가능합니다.
 * 하위 클래스의 @PrimaryKeyJoinColumn 주석은 선택사항이며 외래 키 매핑을 사용자 정의해야 하는 경우에만 사용됩니다. 상위 클래스에 '@Id' 필드가 있고 상속 전략이 정의된 경우 위 설정이 올바르게 작동해야 합니다.
 */
@Slf4j
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PROTECTED)
@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id") // 상속받은 엔티티의 기본 키를 지정
public class Student extends LoginCredential {

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String email = "";

    @OneToMany(mappedBy = "student")
    private List<StudentQuiz> studentQuizzes = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StudentCurriculum> studentCurricula = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StudentLecture> studentLectures = new ArrayList<>();

    public static Student of(String username, String password, String nickname, String email) {
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(password);
        student.setEmail(email);
        student.name = nickname;

        return student;
    }

    public void addCurriculum(StudentCurriculum studentCurriculum) {
        this.studentCurricula.add(studentCurriculum);
    }

    public void updateInfo(String nickname, String email, String password) {
        if (nickname != null && !nickname.equals("")) {
            this.name = nickname;
        }

        if (email != null && !email.equals("")) {
            this.email = email;
        }

        if (password != null & !password.equals("")) {
            this.setPassword(password);
        }
    }

    public void addStudentLecture(StudentLecture studentLecture) {
        this.studentLectures.add(studentLecture);
    }
}
