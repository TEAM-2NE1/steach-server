package com.twentyone.steachserver.domain.student.service;

import com.twentyone.steachserver.domain.student.dto.StudentDto;
import com.twentyone.steachserver.domain.student.model.Student;
import com.twentyone.steachserver.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;

    @Override
    public Optional<Student> findStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student student = convertToEntity(studentDto);
        student = studentRepository.save(student);
        return convertToDto(student);
    }

    // Conversion methods
    private StudentDto convertToDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getName(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }

    private Student convertToEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setCreatedAt(studentDto.getCreatedAt());
        student.setUpdatedAt(studentDto.getUpdatedAt());
        return student;
    }
}
