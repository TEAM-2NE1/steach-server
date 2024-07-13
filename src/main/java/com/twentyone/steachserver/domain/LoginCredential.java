package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Entity
@Table(name = "login_credentials")
public class LoginCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
}
