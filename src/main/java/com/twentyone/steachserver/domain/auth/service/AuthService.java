package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.SignUpDto;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.auth.repository.LoginCredentialRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final LoginCredentialRepository loginCredentialRepository;

    public String login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        LoginCredential loginCredential = loginCredentialRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("로그인 실패"));

        String accessToken = jwtService.generateAccessToken(loginCredential);

        return accessToken;
    }

    public void signUpStudent(SignUpDto signupDto) {
        //TODO auth code 확인

        //password 인코딩
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());

        //loginCredential 저장
        LoginCredential loginCredential = LoginCredential.of(signupDto.getUsername(), encodedPassword);
        loginCredentialRepository.save(loginCredential);

        //student 저장
        Student student = Student.of(loginCredential, signupDto.getName());
        studentRepository.save(student);
    }

    public void signUpTeacher(SignUpDto signUpDto) {
        //TODO 인증파일 저장
    }
}
