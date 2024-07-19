package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.studentLecture.dto.StudentLectureStatisticDto;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;

import java.math.BigDecimal;

public record GPTDataByLectureDto (String curriculumTitle,
                                   String lectureName,
                                   CurriculumCategory category,
                                   Integer averageQuizTotalScore,
                                   Integer averageQuizAnswerCount,
                                   Integer averageFocusTime,
                                   BigDecimal averageFocusRatio) {

    public static GPTDataByLectureDto of(Curriculum curriculum,
                                         Lecture lecture,
                                         StudentLectureStatisticDto studentLectureStatisticDto) {

        return new GPTDataByLectureDto(curriculum.getTitle(),
                lecture.getTitle(),
                curriculum.getCategory(),
                studentLectureStatisticDto.averageQuizTotalScore(),
                studentLectureStatisticDto.averageQuizAnswerCount(),
                studentLectureStatisticDto.averageFocusTime(),
                studentLectureStatisticDto.averageFocusRatio());
    }
}
