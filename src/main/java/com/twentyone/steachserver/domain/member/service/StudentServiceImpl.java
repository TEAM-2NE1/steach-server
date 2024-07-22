package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.member.dto.StudentInfoRequest;
import com.twentyone.steachserver.domain.member.dto.StudentInfoResponse;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public StudentInfoResponse getInfo(Student student) {
        return StudentInfoResponse.fromDomain(student);
    }

    @Override
    @Transactional
    public StudentInfoResponse updateInfo(StudentInfoRequest request, Student student) {
        student.updateInfo(request.getName(), request.getEmail());

        return StudentInfoResponse.fromDomain(student);
    }
}
