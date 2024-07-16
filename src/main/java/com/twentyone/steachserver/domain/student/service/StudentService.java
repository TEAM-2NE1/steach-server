package com.twentyone.steachserver.domain.student.service;

import com.twentyone.steachserver.domain.student.dto.StudentDto;
import com.twentyone.steachserver.domain.student.model.Student;
import java.util.Optional;

public interface StudentService {
    public Optional<Student> findStudentById(Integer id);

    public StudentDto createStudent(StudentDto studentDto);
}
