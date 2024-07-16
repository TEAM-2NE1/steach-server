package com.twentyone.steachserver.domain.student.model;

import com.twentyone.steachserver.domain.LoginCredential;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter(value = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Student {
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

    public void setName(String name){
        this.name = name;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }
}
