package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculaSearchCondition;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;

public interface CurriculumService {
    CurriculumDetailResponse getDetail(Integer id);

    CurriculumDetailResponse create(LoginCredential credential, CurriculumAddRequest request);

    void registration(LoginCredential credential, Integer curriculaId);

    CurriculumListResponse getMyCourses(LoginCredential credential);

    CurriculumListResponse search(CurriculaSearchCondition condition);
}
