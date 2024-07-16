package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.member.model.Student;

import java.util.Optional;

public interface StudentService {
    Optional<Student> findStudentById(Integer id);
}
