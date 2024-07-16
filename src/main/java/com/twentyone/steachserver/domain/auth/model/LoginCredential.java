package com.twentyone.steachserver.domain.auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@NoArgsConstructor
@Entity
@Table(name = "login_credentials")
public class LoginCredential implements UserDetails {
    private static final Logger log = LoggerFactory.getLogger(LoginCredential.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 16, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    public static LoginCredential of(String username, String password) {
        LoginCredential loginCredential = new LoginCredential();
        loginCredential.username = username;
        loginCredential.password = password;

        return loginCredential;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
