package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "login_credentials_id")
    private LoginCredential loginCredential;

    @Column(length = 30)
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
