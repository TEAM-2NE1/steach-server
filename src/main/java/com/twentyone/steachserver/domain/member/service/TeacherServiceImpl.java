package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.auth.dto.CheckUsernameAvailableResponse;
import com.twentyone.steachserver.domain.auth.service.PasswordAuthTokenService;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.repository.TeacherRepository;
import com.twentyone.steachserver.global.error.ResourceNotFoundException;
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
    private final TeacherRepository teacherRepository;

    @Override
    public TeacherInfoResponse getInfo(Teacher teacher) {
        return TeacherInfoResponse.fromDomain(teacher);
    }

    @Override
    public TeacherInfoResponse getCommonInfo(Integer teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return TeacherInfoResponse.fromDomainForAll(teacher);
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

    @Override
    public CheckUsernameAvailableResponse checkEmailAvailability(String email) {
        boolean canUse = !teacherRepository.existsByEmail(email);

        return new CheckUsernameAvailableResponse(canUse);
    }
}
