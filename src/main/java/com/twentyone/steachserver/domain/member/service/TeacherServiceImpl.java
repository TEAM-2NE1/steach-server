package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.auth.service.PasswordAuthTokenService;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final PasswordAuthTokenService passwordAuthTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TeacherInfoResponse getInfo(Teacher teacher) {
        return TeacherInfoResponse.fromDomain(teacher);
    }

    @Transactional
    @Override
    public TeacherInfoResponse updateInfo(TeacherInfoRequest request, Teacher teacher) {
        //임시토큰 검증
        passwordAuthTokenService.validateToken(request.getPasswordAuthToken(), teacher);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        teacher.updateInfo(request.getNickname(), request.getEmail(), request.getBriefIntroduction(), request.getAcademicBackground(), request.getSpecialization(), encodedPassword);

        return TeacherInfoResponse.fromDomain(teacher);
    }
}
