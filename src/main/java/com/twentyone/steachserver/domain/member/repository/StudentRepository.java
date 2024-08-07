package com.twentyone.steachserver.domain.member.repository;

import com.twentyone.steachserver.domain.member.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByName(String name);
    boolean existsByName(String nickname);
}
