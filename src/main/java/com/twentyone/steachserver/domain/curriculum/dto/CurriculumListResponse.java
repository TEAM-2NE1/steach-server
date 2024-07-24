package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumListResponse {
    private List<CurriculumDetailResponse> curricula = new ArrayList<>();

    public static CurriculumListResponse fromDomainList(List<Curriculum> curriculaList) {
        CurriculumListResponse response = new CurriculumListResponse();
        List<CurriculumDetailResponse> responseList = response.curricula;

        for (Curriculum curriculum : curriculaList) {
            responseList.add(CurriculumDetailResponse.fromDomain(curriculum));
        }

        return response;
    }
}
