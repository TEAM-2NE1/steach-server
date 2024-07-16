package com.twentyone.steachserver.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    @Override
    public Optional<Student> findStudentById(Integer id) {
        return studentRepository.findById(id);
    }
}
