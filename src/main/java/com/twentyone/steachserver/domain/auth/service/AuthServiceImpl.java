package com.twentyone.steachserver.domain.auth.service;

import com.twentyone.steachserver.domain.auth.dto.*;
import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.auth.repository.LoginCredentialRepository;
import com.twentyone.steachserver.domain.member.model.Admin;
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

        Role role = null;
        String username = null;
        String name = null;
        String email = null;

        if (loginCredential instanceof Admin admin) {
            role = Role.ADMIN;
            name = admin.getName();
        } else if (loginCredential instanceof Teacher teacher) {
            role = Role.TEACHER;
            name = teacher.getName();
            email = teacher.getEmail();
        } else if (loginCredential instanceof Student student)  {
            role = Role.STUDENT;
            name = student.getName();
            email = student.getEmail();
        } else {
            throw new RuntimeException("에러");
        }

        String accessToken = jwtService.generateAccessToken(loginCredential);

        return LoginResponseDto.of(accessToken, role, loginCredential.getUsername(), name, email);
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
        authCodeService.validateAndApply(studentSignUpDto.getAuth_code());

        //password 인코딩
        String encodedPassword = passwordEncoder.encode(studentSignUpDto.getPassword());

        //student 저장
        Student student = Student.of(studentSignUpDto.getUsername(), encodedPassword, studentSignUpDto.getNickname(), studentSignUpDto.getEmail());
        studentRepository.save(student);

        String accessToken = jwtService.generateAccessToken(student);

        return LoginResponseDto.of(accessToken, Role.STUDENT, student.getUsername(), student.getName(), student.getEmail());
    }

    @Override
    @Transactional
    public LoginResponseDto signUpTeacher(TeacherSignUpDto teacherSignUpDto, MultipartFile file) throws IOException {
        validateUserName(teacherSignUpDto.getUsername());

        //인증파일 저장
        String fileName = null;
        if (file != null) {
            fileName = FileUtil.storeFile(file, uploadDir);
        }

        //password 인코딩
        String encodedPassword = passwordEncoder.encode(teacherSignUpDto.getPassword());

        //Teacher 저장
        Teacher teacher = Teacher.of(teacherSignUpDto.getUsername(), encodedPassword, teacherSignUpDto.getNickname(),
                teacherSignUpDto.getEmail(), fileName);
        teacherRepository.save(teacher);

        String accessToken = jwtService.generateAccessToken(teacher);

        return LoginResponseDto.of(accessToken, Role.TEACHER, teacher.getUsername(), teacher.getName(), teacher.getEmail());
    }

    @Override
    public CheckUsernameAvailableResponse checkUsernameAvailability(String username) {
        boolean canUse = !loginCredentialRepository.existsByUsername(username);

        return new CheckUsernameAvailableResponse(canUse);
    }

    @Override
    public MemberCheckPasswordResponseDto checkPassword(LoginCredential loginCredential, MemberCheckPasswordRequestDto checkPasswordRequestDto) {
        String password = checkPasswordRequestDto.password();

        //TODO OSIV 성능관련 찾아보기
//        LoginCredential realLoginCredential= loginCredentialRepository.findByUsername(loginCredential.getUsername())
//                .orElseThrow(() -> new RuntimeException("없는 사용자 입니다."));

        String realPasswordEncode = loginCredential.getPassword();

        if (!passwordEncoder.matches(password, realPasswordEncode)) {
            throw new ForbiddenException("패스워드 일치하지 않음");
        }

        return MemberCheckPasswordResponseDto.of(jwtService.generatePasswordAuthToken(loginCredential));
    }

    @Override
    public void deleteMember(LoginCredential loginCredential) {
        loginCredentialRepository.delete(loginCredential);
    }
}
