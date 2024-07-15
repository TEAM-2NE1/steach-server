package com.twentyone.steachserver.domain.auth.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "auth_codes")
public class AuthCode {
    @Id @Column(length = 30)
    private String authCode;

    @Column(nullable = false)
    private Boolean isRegistered = false;
}
