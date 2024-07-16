package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "teachers")
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer volunteerTime = 0;

    @Column(length = 255)
    private String pathQualification;

    @OneToOne
    @JoinColumn(name = "login_credential_id", referencedColumnName = "id", nullable = false)
    private LoginCredential loginCredential;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static Teacher of(LoginCredential loginCredential, String name, String pathQualification) {
        Teacher teacher = new Teacher();
        teacher.loginCredential = loginCredential;
        teacher.name = name;
        teacher.pathQualification = pathQualification;

        return teacher;
    }
}
