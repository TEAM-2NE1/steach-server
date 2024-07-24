package com.twentyone.steachserver.domain.auth.repository;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginCredentialRepository extends JpaRepository<LoginCredential, Integer> {
    Optional<LoginCredential> findByUsername(String username);

    boolean existsByUsername(String username);
}
