package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "id") // 상속받은 엔티티의 기본 키를 지정\
public class Admin extends LoginCredential{
    @Column(length = 30, nullable = false)
    private String name;

}
