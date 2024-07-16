package com.twentyone.steachserver.domain.member.repository;

import com.twentyone.steachserver.domain.member.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
