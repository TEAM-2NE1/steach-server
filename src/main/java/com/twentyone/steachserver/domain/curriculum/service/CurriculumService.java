package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculaSearchCondition;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import org.springframework.transaction.annotation.Transactional;

public interface CurriculumService {
    CurriculumDetailResponse getDetail(Integer id);

    CurriculumDetailResponse create(LoginCredential credential, CurriculumAddRequest request);

    void registration(LoginCredential credential, Integer curriculaId);

    @Transactional(readOnly = true)
    CurriculumListResponse getTeachersCurricula(Teacher teacher);

    @Transactional(readOnly = true)
    CurriculumListResponse getStudentsCurricula(Student student);

    CurriculumListResponse search(CurriculaSearchCondition condition);
}
