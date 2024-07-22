package com.twentyone.steachserver.domain.member.service;

import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;

public interface TeacherService {
    TeacherInfoResponse getInfo(Teacher teacher);
}
