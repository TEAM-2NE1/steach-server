package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.dto.radarChartStatisticDto;
import com.twentyone.steachserver.domain.statistic.dto.GPTDataRequestDto;
import com.twentyone.steachserver.domain.statistic.model.mongo.GPTDataByLecture;
import com.twentyone.steachserver.domain.statistic.model.mongo.LectureStatisticsByAllStudent;
import com.twentyone.steachserver.domain.statistic.dto.temp.LectureStatisticsByStudentDto;
import com.twentyone.steachserver.domain.statistic.repository.GPTDataByLectureMongoRepository;
import com.twentyone.steachserver.domain.statistic.repository.LectureStatisticMongoRepository;
import com.twentyone.steachserver.domain.statistic.repository.RadarChartStatisticRepository;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final CurriculumRepository curriculumRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;

    private final RadarChartStatisticRepository radarChartStatisticRepository;
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
     *
     * @return 7 numbers
     */


// HACK : 응급조치로 우선 하드코딩을
    @Override
    public radarChartStatisticDto getStatistics(Integer studentId) {
        radarChartStatisticDto statisticsDto = new radarChartStatisticDto();

        double[] listAvgFocusRatio = new double[NUMBER_OF_CATEGORIES];
        int[] listLectureMinutes = new int[NUMBER_OF_CATEGORIES];

        /*
         * 여기에 students_statistics 23개의 column을 가져오는 코드 작성
         * */

        double maxFocusRatio = -1;
        int maxLectureMinutes = -1;

        int[] retStatistics = new int[NUMBER_OF_CATEGORIES];

        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            if (maxFocusRatio > listAvgFocusRatio[i]) {
                maxFocusRatio = listAvgFocusRatio[i];
            }
            if (maxLectureMinutes > listLectureMinutes[i]) {
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


        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            retStatistics[i] = (int) ((listAvgFocusRatio[i] * factorWeightingFocusRatio) + (listLectureMinutes[i] * factorWeightingLectureMinutes));
        }

        // 카테고리가 현행 7개 이므로 하드코딩(...)
        statisticsDto.setItem1(retStatistics[0]);
        statisticsDto.setItem2(retStatistics[1]);
        statisticsDto.setItem3(retStatistics[2]);
        statisticsDto.setItem4(retStatistics[3]);
        statisticsDto.setItem5(retStatistics[4]);
        statisticsDto.setItem6(retStatistics[5]);
        statisticsDto.setItem7(retStatistics[6]);

        // 저장 부탁드릴게요!
//        radarChartStatisticRepository.save(statistics);

        return statisticsDto;
    }

    @Override
    public String createGPTString(Student student, GPTDataRequestDto gptDataRequestDto) {
//        XXX: 영어로 작성했으면 좋겠어요!
        StringBuilder sb = new StringBuilder();
        sb.append("I want career recommendations.");


        List<Integer> lectureIds = gptDataRequestDto.lectureIds();
        for (Integer lectureId : lectureIds) {
            GPTDataByLecture statistic = getGPTStatistic(lectureId, student.getId());

//            TODO:  로직 만들어야함 !!!!
            sb.append("\n");
            sb.append(statistic);
        }

        sb.append("I hope the answer begins with ").append(student.getName()).append("’s career recommendation results.");
        sb.append("in korean");
        return sb.toString();
    }

    private GPTDataByLecture getGPTStatistic(Integer lectureId, Integer studentId) {
        return gptDataByLectureMongoRepository.findByLectureIdAndStudentId(lectureId, studentId)
                .orElseThrow(() -> new IllegalArgumentException("lectureId : " + lectureId + " does not exist"));

    }

    @Override
    public Optional<LectureStatisticsByAllStudent> getLectureStatisticsByAllStudent(Integer lectureId) {
        return lectureStatisticMongoRepository.findByLectureId(lectureId);

    }

    @Override
    @Transactional
    public void createStatisticsByFinalLecture(Lecture lecture) {
        List<StudentLecture> allStudentInfoByLectureId = studentLectureQueryRepository.findAllStudentInfoByLectureId(lecture.getId());
        createLectureStatisticsByAllStudent(lecture, allStudentInfoByLectureId);
        createGPTData(lecture, allStudentInfoByLectureId);
    }


    // 음.. 먼진 모르겠지만 protected 써야지 Trancsacion 사용가능했음.
    @Transactional
    protected void createLectureStatisticsByAllStudent(Lecture lecture, List<StudentLecture> allStudentInfoByLectureId) {
        LectureStatisticsByAllStudent lectureStatisticsByAllStudent = LectureStatisticsByAllStudent.of(lecture, allStudentInfoByLectureId);
        lectureStatisticMongoRepository.save(lectureStatisticsByAllStudent);
    }

    @Transactional
    protected void createGPTData(Lecture lecture, List<StudentLecture> allStudentInfoByLectureId) {
        Curriculum curriculum = curriculumRepository.findByLecturesContaining(lecture)
                .orElseThrow(() -> new IllegalStateException("curriculum not found"));

        // 학생별로 데이터를 모으기 위한 맵
        Map<Integer, LectureStatisticsByStudentDto> studentStatisticsMap = new HashMap<>();

        for (StudentLecture studentLecture : allStudentInfoByLectureId) {
            Student student = studentLecture.getStudent();

            studentStatisticsMap
                    .computeIfAbsent(student.getId(), k -> LectureStatisticsByStudentDto.of(student, lecture))
                    .addLectureData(studentLecture);
        }

        for (LectureStatisticsByStudentDto lectureStatisticsByStudent : studentStatisticsMap.values()) {
            GPTDataByLecture gptDataByLecture = GPTDataByLecture.of(lecture, curriculum, lectureStatisticsByStudent);
            gptDataByLectureMongoRepository.save(gptDataByLecture);
        }
    }
}
