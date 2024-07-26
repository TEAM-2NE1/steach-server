package com.twentyone.steachserver.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PageableDto {
    protected Integer currentPageNumber; //현재 페이지
    protected Integer totalPage;  //전체 페이지 개수
    protected Integer pageSize; //페이지 크기(n개씩 보기)
}
