package com.twentyone.steachserver.domain.curriculum.dto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;

public record SimpleCurriculumByLectureDto(String curriculumTitle,String curriculumCategory) {

    public static SimpleCurriculumByLectureDto createSimpleCurriculumByLectureDto(Curriculum curriculum) {
        return new SimpleCurriculumByLectureDto(curriculum.getTitle(), curriculum.getCategory().toString());
    }
}
