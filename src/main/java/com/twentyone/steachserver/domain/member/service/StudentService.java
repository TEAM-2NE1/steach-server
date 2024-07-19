package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.member.model.Student;

import java.util.Optional;

public interface StudentService {
    public Optional<Student> findStudentById(Integer id);
    public Optional<Student> findStudentByUsername(String username);
}
