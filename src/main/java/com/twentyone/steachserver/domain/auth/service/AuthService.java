package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.LoginResponseDto;
import com.twentyone.steachserver.domain.auth.dto.SignUpDto;

public interface AuthService {
    LoginResponseDto login(LoginDto loginDto);

    void signUpStudent(SignUpDto signupDto);

    void signUpTeacher(SignUpDto signUpDto);
}
