package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "auth_codes")
public class AuthCode {
    @Id
    private String authCode;

    private Integer isRegistered;
}
