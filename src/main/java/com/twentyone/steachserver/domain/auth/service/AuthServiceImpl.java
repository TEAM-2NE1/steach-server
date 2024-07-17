package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.LoginDto;
import com.twentyone.steachserver.domain.auth.dto.LoginResponseDto;
import com.twentyone.steachserver.domain.auth.dto.StudentSignUpDto;
import com.twentyone.steachserver.domain.auth.dto.TeacherSignUpDto;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.auth.repository.LoginCredentialRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.member.repository.TeacherRepository;
import com.twentyone.steachserver.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final LoginCredentialRepository loginCredentialRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        LoginCredential loginCredential = loginCredentialRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("로그인 실패"));

        String accessToken = jwtService.generateAccessToken(loginCredential);

        return LoginResponseDto.builder()
                .token(accessToken)
                .build();
    }

    @Override
    public void signUpStudent(StudentSignUpDto signupDtoStudent) {
        //TODO auth code 확인

        //password 인코딩
        String encodedPassword = passwordEncoder.encode(signupDtoStudent.getPassword());

        //loginCredential 저장
        LoginCredential loginCredential = LoginCredential.of(signupDtoStudent.getUsername(), encodedPassword);
        loginCredentialRepository.save(loginCredential);

        //student 저장
        Student student = Student.of(loginCredential, signupDtoStudent.getName());
        studentRepository.save(student);
    }

    @Override
    public void signUpTeacher(TeacherSignUpDto signupDtoStudent, MultipartFile file) throws IOException {
        //TODO 인증파일 저장
        String fileName = FileUtil.storeFile(file, uploadDir);

        //password 인코딩
        String encodedPassword = passwordEncoder.encode(signupDtoStudent.getPassword());

        //loginCredential 저장
        LoginCredential loginCredential = LoginCredential.of(signupDtoStudent.getUsername(), encodedPassword);
        loginCredentialRepository.save(loginCredential);

        //Teacher 저장
        Teacher teacher = Teacher.of(loginCredential, signupDtoStudent.getName(), fileName);
        teacherRepository.save(teacher);
    }
}
