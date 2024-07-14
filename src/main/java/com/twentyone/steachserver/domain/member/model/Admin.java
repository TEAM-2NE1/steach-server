package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.member.auth.model.LoginCredential;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    @OneToOne
    @JoinColumn(name = "login_credentials_id")
    private LoginCredential loginCredential;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
