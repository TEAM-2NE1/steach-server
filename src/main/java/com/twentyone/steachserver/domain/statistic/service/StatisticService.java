package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.dto.RadarChartStatisticDto;
import com.twentyone.steachserver.domain.statistic.model.mongo.LectureStatisticsByAllStudent;

import java.util.List;

public interface StatisticService {
    RadarChartStatisticDto getRadarChartStatistic(Integer studentId);

    String createGPTString(Student student);

    List<LectureStatisticsByAllStudent> getLectureStatisticsByAllStudent(Integer lectureId);

    void createStatisticsByFinalLecture(Lecture lecture);
}
