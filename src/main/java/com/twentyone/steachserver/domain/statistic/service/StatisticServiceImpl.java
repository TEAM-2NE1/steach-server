package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private StatisticRepository statisticRepository;
    private StudentService studentService;
    private LectureService lectureService;
    private CurriculumService curriculumService;

    final int NUMBER_OF_CATEGORIES = 7;

    final int WEIGHT_FOCUS_RATIO = 15;
    final int WEIGHT_LECTURE_MINUTES = 85;


    /**
     * Each item means a focus ratio for one category
     * (e.g. 76 for Korean, 81 for Math. 98 for Engineering)
     * Since category names are not settled, just returning 7 numbers.
     * 각 item은 focus ratio를 의미.
     * (예시. 국어 76, 수학 81, 공학 98)
     * 카테고리명이 아직 정해지지 않았기 때문에 숫자 7개의 숫자를 반환.
     * @return 7 numbers
     */

    public StatisticsDto getStatistics(int studentId){
        StatisticsDto statisticsDto = new StatisticsDto();

        double[] listAvgFocusRatio = new double[NUMBER_OF_CATEGORIES];
        int[] listLectureMinutes = new int[NUMBER_OF_CATEGORIES];

        /*
        * 여기에 students_statistics 23개의 column을 가져오는 코드 작성
        * */

        double maxFocusRatio = -1;
        int maxLectureMinutes = -1;

        int[] retStatistics = new int[NUMBER_OF_CATEGORIES];

        for(int i = 0; i < NUMBER_OF_CATEGORIES; i++){
            if(maxFocusRatio > listAvgFocusRatio[i]){
                maxFocusRatio = listAvgFocusRatio[i];
            }
            if(maxLectureMinutes > listLectureMinutes[i]){
                maxLectureMinutes = listLectureMinutes[i];
            }
        }

        /* 각 focus_ratio(집중도)에 가중치를 반영하기 위해 필요한 요소.
           NUM_OF_CATEGORIES(현행 7)개의 카테고리 중 가장 높은 focus_ratio가 만점.
           가장 높은 focus_ratio가 WEIGHT_FOCUS_RATIO(현행 15)점.
           (WEIGHT_FOCUS_RATIO/maxFocusRatio)로 계산된 값을
           NUM_OF_CATEGORIES개의 focus_ratio에 각각 곱.
            - Also applies to 'factorWeightingLectureMinutes'
           focus_ratio의 최대값과 lecture_minutes의 최대값이 다를 수 있음.
           이 경우 100.00이 나오지 않을 수 있음.
         */
        double factorWeightingFocusRatio = (WEIGHT_FOCUS_RATIO / maxFocusRatio);
        double factorWeightingLectureMinutes = ((double) WEIGHT_LECTURE_MINUTES / maxLectureMinutes);


        for(int i = 0; i < NUMBER_OF_CATEGORIES; i++){
            retStatistics[i] = (int)((listAvgFocusRatio[i] * factorWeightingFocusRatio) + (listLectureMinutes[i] * factorWeightingLectureMinutes));
        }

        // 카테고리가 현행 7개 이므로 하드코딩(...)
        statisticsDto.setItem1(retStatistics[0]);
        statisticsDto.setItem2(retStatistics[1]);
        statisticsDto.setItem3(retStatistics[2]);
        statisticsDto.setItem4(retStatistics[3]);
        statisticsDto.setItem5(retStatistics[4]);
        statisticsDto.setItem6(retStatistics[5]);
        statisticsDto.setItem7(retStatistics[6]);

        return statisticsDto;
    }

    @Override
    public StatisticsDto getStatistics(String studentUsername) {
        return null;
    }
}
