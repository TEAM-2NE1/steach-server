package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CurriculaSearchCondition {
    private CurriculumCategory curriculumCategory; //카테고리
    private CurriculaOrderType order; //정렬기준: 최신순, 인기순?
    private Boolean onlyAvailable; //신청가능한 것만 보기
    private String search; //검색어
}
