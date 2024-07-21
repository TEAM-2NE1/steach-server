package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.dto.StatisticsDto;
import com.twentyone.steachserver.domain.statistic.model.GPTDataByLecture;
import com.twentyone.steachserver.domain.statistic.model.LectureStatisticsByAllStudent;
import com.twentyone.steachserver.domain.statistic.model.LectureStatisticsByStudent;
import com.twentyone.steachserver.domain.statistic.repository.GPTDataByLectureMongoRepository;
import com.twentyone.steachserver.domain.statistic.repository.LectureStatisticMongoRepository;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private final LectureRepository lectureRepository;
    private final CurriculumRepository curriculumRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;

    private final LectureStatisticMongoRepository lectureStatisticMongoRepository;
    private final GPTDataByLectureMongoRepository gptDataByLectureMongoRepository;

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

    @Override
    public StatisticsDto getStatistics(Integer studentId){
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
    @Transactional
    public void createStatisticsByFinalLecture(Lecture lecture) {
        List<StudentLecture> allStudentInfoByLectureId = studentLectureQueryRepository.findAllStudentInfoByLectureId(lecture.getId());
        createLectureStatisticsByAllStudent(lecture, allStudentInfoByLectureId);
        createGPTData(lecture, allStudentInfoByLectureId);
    }


    @Transactional
    public void createLectureStatisticsByAllStudent(Lecture lecture, List<StudentLecture> allStudentInfoByLectureId) {
        int studentCount = allStudentInfoByLectureId.size();

        Integer totalQuizTotalScore = 0;
        Integer totalQuizAnswerCount = 0;
        Integer totalFocusTime = 0;
        BigDecimal totalFocusRatio = BigDecimal.valueOf(0);


        for (StudentLecture studentLecture : allStudentInfoByLectureId) {
            totalQuizTotalScore += studentLecture.getQuizTotalScore();
            totalQuizAnswerCount += studentLecture.getQuizAnswerCount();
            totalFocusRatio = totalFocusRatio.add(studentLecture.getFocusRatio());
            totalFocusTime += studentLecture.getFocusTime();
        }

        Integer averageQuizTotalScore = totalQuizTotalScore / studentCount;
        Integer averageQuizAnswerCount = totalQuizAnswerCount / studentCount;
        Integer averageFocusTime = totalFocusTime / studentCount;
        BigDecimal averageFocusRatio = totalFocusRatio.divide(BigDecimal.valueOf(studentCount), 2, RoundingMode.HALF_UP);

        LectureStatisticsByAllStudent lectureStatisticsByAllStudent = LectureStatisticsByAllStudent.of(lecture, averageQuizTotalScore, averageQuizAnswerCount, averageFocusTime, averageFocusRatio);
        lectureStatisticMongoRepository.save(lectureStatisticsByAllStudent);
    }

    private void createGPTData(Lecture lecture, List<StudentLecture> allStudentInfoByLectureId) {
        Curriculum curriculum = curriculumRepository.findByLecturesContaining(lecture)
                .orElseThrow(() -> new IllegalStateException("curriculum not found"));

        // 학생별로 데이터를 모으기 위한 맵
        Map<Integer, LectureStatisticsByStudent> studentStatisticsMap = new HashMap<>();

        for (StudentLecture studentLecture : allStudentInfoByLectureId) {
            Student student = studentLecture.getStudent();

            studentStatisticsMap
                    .computeIfAbsent(student.getId(), k -> LectureStatisticsByStudent.of(student, lecture))
                    .addLectureData(studentLecture);
        }

        for (LectureStatisticsByStudent lectureStatisticsByStudent : studentStatisticsMap.values()) {
            GPTDataByLecture gptDataByLecture = GPTDataByLecture.of(lecture, curriculum, lectureStatisticsByStudent);
            gptDataByLectureMongoRepository.save(gptDataByLecture);
        }
    }
}
