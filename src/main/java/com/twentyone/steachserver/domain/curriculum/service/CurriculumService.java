package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;

public interface CurriculumService {
    CurriculumDetailResponse getDetail(Integer id);
}
