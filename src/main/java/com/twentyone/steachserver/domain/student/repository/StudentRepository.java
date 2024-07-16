package com.twentyone.steachserver.domain.student.repository;

import com.twentyone.steachserver.domain.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
