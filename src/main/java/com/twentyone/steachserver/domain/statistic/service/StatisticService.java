package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.dto.radarChartStatisticDto;
import com.twentyone.steachserver.domain.statistic.dto.GPTDataRequestDto;
import com.twentyone.steachserver.domain.statistic.model.LectureStatisticsByAllStudent;

import java.util.Optional;

public interface StatisticService {
    radarChartStatisticDto getStatistics(Integer studentId);

    String createGPTString(Student student, GPTDataRequestDto gptDataRequestDto);

    Optional<LectureStatisticsByAllStudent> getLectureStatisticsByAllStudent(Integer lectureId);

    void createStatisticsByFinalLecture(Lecture lecture);
}
