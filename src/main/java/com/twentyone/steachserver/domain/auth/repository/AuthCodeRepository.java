package com.twentyone.steachserver.domain.auth.repository;

import com.twentyone.steachserver.domain.auth.model.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthCode, String> {
}
