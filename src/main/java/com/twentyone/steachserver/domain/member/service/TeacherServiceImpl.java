package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.auth.service.JwtService;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final JwtService jwtService;

    @Override
    public TeacherInfoResponse getInfo(Teacher teacher) {
        return TeacherInfoResponse.fromDomain(teacher);
    }

    @Transactional
    @Override
    public TeacherInfoResponse updateInfo(TeacherInfoRequest request, Teacher teacher) {
        //임시토큰 검증
        if (!jwtService.isTempTokenValid(request.getTempToken(), teacher)) {
            throw new ForbiddenException("토큰이 유효하지 않음");
        }

        teacher.updateInfo(request.getName(), request.getEmail(), request.getBriefIntroduction(), request.getAcademicBackground(), request.getSpecialization());

        return TeacherInfoResponse.fromDomain(teacher);
    }
}
