package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.auth.service.TempTokenService;
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
    private final TempTokenService tempTokenService;

    @Override
    public TeacherInfoResponse getInfo(Teacher teacher) {
        return TeacherInfoResponse.fromDomain(teacher);
    }

    @Transactional
    @Override
    public TeacherInfoResponse updateInfo(TeacherInfoRequest request, Teacher teacher) {
        //임시토큰 검증
        tempTokenService.validateToken(request.getTempToken(), teacher);

        teacher.updateInfo(request.getName(), request.getEmail(), request.getBriefIntroduction(), request.getAcademicBackground(), request.getSpecialization());

        return TeacherInfoResponse.fromDomain(teacher);
    }
}
