package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;

import java.util.Optional;

public interface CurriculumService {
    Optional<Curriculum> findById(Integer id);

    CurriculumDetailResponse getDetail(Integer id);

    CurriculumDetailResponse create(LoginCredential credential, CurriculumAddRequest request);

    void registration(LoginCredential credential, Integer curriculaId);

    CurriculumListResponse getMyCourses(LoginCredential credential);
}
