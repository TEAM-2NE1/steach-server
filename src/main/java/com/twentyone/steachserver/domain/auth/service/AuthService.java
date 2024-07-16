package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.LoginResponseDto;
import com.twentyone.steachserver.domain.auth.dto.StudentSignUpDto;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    LoginResponseDto login(LoginDto loginDto);

    void signUpStudent(StudentSignUpDto studentSignUpDto);

    void signUpTeacher(TeacherSignUpDto teacherSignUpDto, MultipartFile file) throws IOException;
}
