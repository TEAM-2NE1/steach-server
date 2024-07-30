package com.twentyone.steachserver.domain.auth.model;

import com.twentyone.steachserver.domain.auth.error.AuthCodeAlreadyInUseException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "auth_codes")
public class AuthCode {
    @Id
    @Column(length = 30)
    private String authCode;

    @Column(nullable = false)
    private Boolean isRegistered = false;

    public static AuthCode of(String authCode) {
        AuthCode newAuthCode = new AuthCode();
        newAuthCode.authCode = authCode;

        return newAuthCode;
    }

    public void register() {
        if (isRegistered) {
            throw new AuthCodeAlreadyInUseException("이미 등록된 authCode");
        }

        this.isRegistered = true;
    }
}