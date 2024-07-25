package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;

import java.util.ArrayList;
import java.util.List;

public record CurriculumListInOrderResponseDto(List<SimpleCurriculumDto> curricula) {
    public static CurriculumListInOrderResponseDto of(List<SimpleCurriculumDto> simpleCurriculumDtoList) {
        return new CurriculumListInOrderResponseDto(simpleCurriculumDtoList);
    }
}
