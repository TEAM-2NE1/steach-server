package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "teachers")
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id") // 상속받은 엔티티의 기본 키를 지정
public class Teacher extends LoginCredential{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    private String email = "";

    @Column(nullable = false)
    private Integer volunteerTime = 0;

    @Column(length = 255)
    private String pathQualification;

    public static Teacher of(String username, String password, String name, String email, String pathQualification) {
        Teacher teacher = new Teacher();
//        teacher.loginCredential = loginCredential;

        teacher.setUsername(username);
        teacher.setPassword(password);
        teacher.name = name;
        teacher.email = email;
        teacher.pathQualification = pathQualification;

        return teacher;
    }
}
