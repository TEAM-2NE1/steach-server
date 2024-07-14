package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "studentds")
@NoArgsConstructor
@Getter(value = AccessLevel.PUBLIC)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "login_credentials_id")
    private LoginCredential loginCredential;

    private String name;
    private Date createdDate;
    private Date updatedDate;
}
