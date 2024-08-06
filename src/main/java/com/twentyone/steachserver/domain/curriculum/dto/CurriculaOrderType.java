package com.twentyone.steachserver.domain.curriculum.dto;

public enum CurriculaOrderType {
    LATEST, //최신순
    POPULAR, //인기순(현재는 신청자 많은 순)
    POPULAR_PER_RATIO // 인기순 (현재 신청자/ 전체 신청자 신청자 많은 순)
}
