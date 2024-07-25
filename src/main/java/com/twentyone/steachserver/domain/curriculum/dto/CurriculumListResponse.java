package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumListResponse {
    private Integer currentPageNumber; //현재 페이지
    private Integer totalPage;  //전체 페이지 개수
    private Integer pageSize; //페이지 크기(n개씩 보기)
    private List<CurriculumDetailResponse> curricula = new ArrayList<>();

    public static CurriculumListResponse fromDomainList(Page<Curriculum> curriculaList) {
        CurriculumListResponse response = new CurriculumListResponse();
        List<CurriculumDetailResponse> responseList = response.curricula;

        for (Curriculum curriculum : curriculaList) {
            responseList.add(CurriculumDetailResponse.fromDomain(curriculum));
        }

        response.currentPageNumber = curriculaList.getPageable().getPageNumber() + 1;
        response.totalPage = curriculaList.getTotalPages();
        response.pageSize = curriculaList.getPageable().getPageSize();

        return response;
    }

    public static CurriculumListResponse fromDomainList(List<Curriculum> curriculaList) {
        CurriculumListResponse response = new CurriculumListResponse();
        List<CurriculumDetailResponse> responseList = response.curricula;

        for (Curriculum curriculum : curriculaList) {
            responseList.add(CurriculumDetailResponse.fromDomain(curriculum));
        }

        return response;
    }
}
