package com.twentyone.steachserver.domain.curriculum.dto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor()
public class SimpleCurriculumByLectureDto {
    private String curriculumTitle;
    private String curriculumCategory;

    public SimpleCurriculumByLectureDto(Curriculum curriculum) {
        this.curriculumTitle = curriculum.getTitle();
        this.curriculumCategory = curriculum.getCategory().toString();
    }
}
