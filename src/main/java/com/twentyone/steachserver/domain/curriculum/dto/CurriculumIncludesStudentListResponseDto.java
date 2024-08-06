package com.twentyone.steachserver.domain.curriculum.dto;

import java.util.List;

public record CurriculumIncludesStudentListResponseDto(List<CurriculumIncludesStudentDto> curriculumListResponse) {
    public static CurriculumIncludesStudentListResponseDto of(List<CurriculumIncludesStudentDto> curriculumListResponse) {
        return new CurriculumIncludesStudentListResponseDto(curriculumListResponse);
    }
}
