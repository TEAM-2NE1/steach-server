package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.dto.LectureStatisticsByAllStudentDto;
import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.dto.GPTDataRequestDto;

public interface StatisticService {
    StatisticsDto getStatistics(Integer studentId);

    String createGPTString(Student student, GPTDataRequestDto gptDataRequestDto);

    LectureStatisticsByAllStudentDto getLectureStatisticsByAllStudent(Integer lectureId);

    void createStatisticsByFinalLecture(Lecture lecture);
}
