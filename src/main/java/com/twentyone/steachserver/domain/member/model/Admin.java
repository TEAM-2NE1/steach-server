package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "login_credential_id", nullable = false, referencedColumnName = "id")
    private LoginCredential loginCredential;
}