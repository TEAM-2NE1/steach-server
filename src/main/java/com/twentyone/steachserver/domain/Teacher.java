package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachers")
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "login_credentials_id")
    private LoginCredential loginCredential;

    @Column(length = 30)
    private String name;
    private Integer volunteerTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String pathQualification;
}
