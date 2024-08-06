package com.twentyone.steachserver.domain.studentCurriculum.dto;

public record IsApplyForCurriculumResponseDto(boolean isApply) {
    public static IsApplyForCurriculumResponseDto of(Boolean isApplyForCurriculum) {
        return new IsApplyForCurriculumResponseDto(isApplyForCurriculum);
    }
}
