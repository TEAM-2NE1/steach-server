package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumDetailRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final CurriculumDetailRepository curriculumDetailRepository;

    @Override
    @Transactional(readOnly = true)
    public CurriculumDetailResponse getDetail(Integer id) {
        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curriculum not found"));

        CurriculumDetail curriculumDetail = curriculum.getCurriculumDetail();

        return CurriculumDetailResponse.builder()
                .title(curriculum.getTitle())
                .subTitle(curriculumDetail.getSubTitle())
                .intro(curriculumDetail.getIntro())
                .information(curriculumDetail.getInformation())
                .category(curriculum.getCategory().name())
                .subCategory(curriculumDetail.getSubCategory())
                .bannerImgUrl(curriculumDetail.getBannerImgUrl())
                .startDate(curriculumDetail.getStartDate().toLocalDate())
                .endDate(curriculumDetail.getEndDate() != null ? curriculumDetail.getEndDate().toLocalDate() : null)
                .weekdaysBitmask(curriculumDetail.getWeekdaysBitmask())
                .lectureStartTime(curriculumDetail.getLectureStartTime().getHour())
                .lectureEndTime(curriculumDetail.getLectureCloseTime().getHour())
                .build();
    }
}
