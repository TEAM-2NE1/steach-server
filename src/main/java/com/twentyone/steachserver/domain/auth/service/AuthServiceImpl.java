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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private static final String ID_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{1,20}$"; //20자 이하

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthCodeService authCodeService;
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

    @Transactional
    public void validateUserName(String username) {
        if (username == null || username.length() == 0) {
            throw new IllegalArgumentException("username은 빈칸 불가능");
        }

        if (!username.matches(ID_PATTERN)) {
            throw new IllegalArgumentException("유효하지 않은 username");
        }

        if (loginCredentialRepository.findByUsername(username)
                .isPresent()) {
            throw new IllegalArgumentException("중복되는 닉네임");
        }
    }

    @Override
    @Transactional
    public LoginResponseDto signUpStudent(StudentSignUpDto studentSignUpDto) {
        validateUserName(studentSignUpDto.getUsername());

        //auth Code 검증
        authCodeService.validate(studentSignUpDto.getAuth_code());

        //password 인코딩
        String encodedPassword = passwordEncoder.encode(studentSignUpDto.getPassword());

        //student 저장
        Student student = Student.of(studentSignUpDto.getUsername(), encodedPassword, studentSignUpDto.getName());
        studentRepository.save(student);

        String accessToken = jwtService.generateAccessToken(student);

        return LoginResponseDto.builder()
                .token(accessToken)
                .build();
    }

    @Override
    @Transactional
    public LoginResponseDto signUpTeacher(TeacherSignUpDto teacherSignUpDto, MultipartFile file) throws IOException {
        validateUserName(teacherSignUpDto.getUsername());

        //TODO 인증파일 저장
        String fileName = FileUtil.storeFile(file, uploadDir);

        //password 인코딩
        String encodedPassword = passwordEncoder.encode(teacherSignUpDto.getPassword());

        //Teacher 저장
        Teacher teacher = Teacher.of(teacherSignUpDto.getUsername(), encodedPassword, teacherSignUpDto.getName(),
                teacherSignUpDto.getEmail(), fileName);
        teacherRepository.save(teacher);

        String accessToken = jwtService.generateAccessToken(teacher);

        return LoginResponseDto.builder()
                .token(accessToken)
                .build();
    }
}
