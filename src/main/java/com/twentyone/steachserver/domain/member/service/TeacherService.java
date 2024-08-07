package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;

public interface TeacherService {
    TeacherInfoResponse getInfo(Teacher teacher);
    TeacherInfoResponse getCommonInfo(Integer teacher);

    TeacherInfoResponse updateInfo(TeacherInfoRequest request, Teacher teacher);

}
