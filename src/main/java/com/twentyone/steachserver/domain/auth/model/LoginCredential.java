package com.twentyone.steachserver.domain.auth.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Entity
@Table(name = "login_credentials")
public class LoginCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 16, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;
}
