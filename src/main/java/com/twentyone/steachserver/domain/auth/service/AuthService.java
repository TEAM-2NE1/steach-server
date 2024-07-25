package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    LoginResponseDto login(LoginDto loginDto);

    LoginResponseDto signUpStudent(StudentSignUpDto studentSignUpDto);

    LoginResponseDto signUpTeacher(TeacherSignUpDto teacherSignUpDto, MultipartFile file) throws IOException;

    CheckUsernameAvailableResponse checkUsernameAvailability(String username);
}
