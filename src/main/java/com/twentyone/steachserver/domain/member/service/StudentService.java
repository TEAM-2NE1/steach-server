package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.member.dto.StudentInfoResponse;
import com.twentyone.steachserver.domain.member.model.Student;

public interface StudentService {
    StudentInfoResponse getInfo(Student student);
}
