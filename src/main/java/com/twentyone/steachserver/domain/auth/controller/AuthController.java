package com.twentyone.steachserver.domain.auth.controller;

import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.LoginResponseDto;
import com.twentyone.steachserver.domain.auth.dto.StudentSignUpDto;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import com.twentyone.steachserver.domain.auth.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/student/join")
    public ResponseEntity<LoginResponseDto> signupStudent(@RequestBody StudentSignUpDto studentSignUpDto) {
        LoginResponseDto loginResponseDto = authServiceImpl.signUpStudent(studentSignUpDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }

    @PostMapping(value = "/teacher/join", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginResponseDto> signupTeacher(@RequestPart("teacherSignUpDto") TeacherSignUpDto teacherSignUpDto, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        LoginResponseDto loginResponseDto = authServiceImpl.signUpTeacher(teacherSignUpDto, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authServiceImpl.login(loginDto));
    }
}
