package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.member.dto.StudentInfoResponse;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public StudentInfoResponse getInfo(Student student) {
        return StudentInfoResponse.fromDomain(student);
    }
}
