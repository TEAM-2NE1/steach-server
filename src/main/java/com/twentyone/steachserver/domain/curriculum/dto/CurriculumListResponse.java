package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.global.dto.PageableDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumListResponse extends PageableDto {
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
    public static CurriculumListResponse fromSimpleDomainList(Page<Curriculum> curriculumList) {
        CurriculumListResponse response = new CurriculumListResponse();
        List<CurriculumDetailResponse> responseList = response.curricula;

        for (Curriculum curriculum : curriculumList) {
            responseList.add(CurriculumDetailResponse.fromDomainBySimple(curriculum));
        }

        response.currentPageNumber = curriculumList.getPageable().getPageNumber() + 1;
        response.totalPage = curriculumList.getTotalPages();
        response.pageSize = curriculumList.getPageable().getPageSize();

        return response;
    }

    public static CurriculumListResponse fromSimpleDomainList(List<Curriculum> curriculumList) {
        CurriculumListResponse response = new CurriculumListResponse();
        List<CurriculumDetailResponse> responseList = response.curricula;

        for (Curriculum curriculum : curriculumList) {
            responseList.add(CurriculumDetailResponse.fromDomainBySimple(curriculum));
        }

        return response;
    }

    public static CurriculumListResponse fromDomainList(List<Curriculum> curriculaList, int pageNumber, int totalPages,
                                                        int pageSize) {
        CurriculumListResponse response = new CurriculumListResponse();
        List<CurriculumDetailResponse> responseList = response.curricula;

        for (Curriculum curriculum : curriculaList) {
            responseList.add(CurriculumDetailResponse.fromDomain(curriculum));
        }

        response.currentPageNumber = pageNumber + 1;
        response.totalPage = totalPages;
        response.pageSize = pageSize;

        return response;
    }


}
