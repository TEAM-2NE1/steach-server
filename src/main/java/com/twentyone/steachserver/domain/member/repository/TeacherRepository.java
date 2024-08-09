package com.twentyone.steachserver.domain.member.repository;

import com.twentyone.steachserver.domain.member.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    boolean existsByEmail(String email);
}
