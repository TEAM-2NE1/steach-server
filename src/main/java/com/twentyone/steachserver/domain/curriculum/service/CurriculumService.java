package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;

public interface CurriculumService {
    CurriculumDetailResponse getDetail(Integer id);

    CurriculumDetailResponse create(LoginCredential credential, CurriculumAddRequest request);
}
