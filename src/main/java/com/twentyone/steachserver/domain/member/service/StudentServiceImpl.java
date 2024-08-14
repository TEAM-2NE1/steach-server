package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.auth.dto.CheckUsernameAvailableResponse;
import com.twentyone.steachserver.domain.auth.service.PasswordAuthTokenService;
import com.twentyone.steachserver.domain.member.dto.StudentInfoRequest;
import com.twentyone.steachserver.domain.member.dto.StudentInfoResponse;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final PasswordAuthTokenService passwordAuthTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StudentInfoResponse getInfo(Student student) {
        return StudentInfoResponse.fromDomain(student);
    }

    @Override
    @Transactional
    public StudentInfoResponse updateInfo(StudentInfoRequest request, Student student) {
        //TODO 403 401 정하기
        passwordAuthTokenService.validateToken(request.getPasswordAuthToken(), student);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        student.updateInfo(request.getNickname(), request.getEmail(), encodedPassword);

        return StudentInfoResponse.fromDomain(student);
    }

    @Override
    public CheckUsernameAvailableResponse checkNicknameAvailability(String nickname) {
        boolean canUse = !studentRepository.existsByName(nickname);

        return new CheckUsernameAvailableResponse(canUse);
    }

    @Override
    public CheckUsernameAvailableResponse checkEmailAvailability(String email) {
        boolean canUse = !studentRepository.existsByEmail(email);

        return new CheckUsernameAvailableResponse(canUse);
    }
}
