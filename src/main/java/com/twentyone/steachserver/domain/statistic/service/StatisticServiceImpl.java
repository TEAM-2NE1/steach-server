package com.twentyone.steachserver.domain.statistic.service;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.statistic.dto.RadarChartStatisticDto;
import com.twentyone.steachserver.domain.statistic.dto.StatisticsByCurriculumCategory;
import com.twentyone.steachserver.domain.statistic.model.RadarChartStatistic;
import com.twentyone.steachserver.domain.statistic.model.mongo.GPTDataByLecture;
import com.twentyone.steachserver.domain.statistic.model.mongo.LectureStatisticsByAllStudent;
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

    final int NUMBER_OF_CATEGORIES = CurriculumCategory.sizeExcludingETC();

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


    @Override
    public RadarChartStatisticDto getRadarChartStatistic(Integer studentId) {
        RadarChartStatistic radarChartStatistic = radarChartStatisticRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("studentId : " + studentId + " 통계가 존재하지 않습니다."));

        List<StatisticsByCurriculumCategory> items = radarChartStatistic.getItems();

        if(items.stream()
                .allMatch(statisticsByCurriculumCategory -> statisticsByCurriculumCategory.totalLectureMinute() == 0)) {
            return RadarChartStatisticDto.of(new ArrayList<>(NUMBER_OF_CATEGORIES));
        }

        BigDecimal maxFocusRatio = BigDecimal.valueOf(-1);
        int maxLectureMinutes = -1;

        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            if (items.get(i).averageFocusRatio().compareTo(maxFocusRatio) > 0) {
                maxFocusRatio = items.get(i).averageFocusRatio();
            }
            if (maxLectureMinutes < items.get(i).totalLectureMinute()) {
                maxLectureMinutes = items.get(i).totalLectureMinute();
            }
        }

        // 이건 기존 값에 곱해줄 값
        List<Integer> list = createRadarChartScores(maxFocusRatio, maxLectureMinutes, items);

        return RadarChartStatisticDto.of(list);
    }

    private List<Integer> createRadarChartScores(BigDecimal maxFocusRatio, int maxLectureMinutes, List<StatisticsByCurriculumCategory> items) {
        BigDecimal factorWeightingFocusRatio = BigDecimal.valueOf(WEIGHT_FOCUS_RATIO).divide(maxFocusRatio, 2, RoundingMode.DOWN);
        double factorWeightingLectureMinutes = (double) WEIGHT_LECTURE_MINUTES / maxLectureMinutes;

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            double weightedFocusRatio = items.get(i).averageFocusRatio().multiply(factorWeightingFocusRatio).intValue();
            double weightedLectureMinutes = items.get(i).totalLectureMinute() * factorWeightingLectureMinutes;
            int sum = (int) (weightedFocusRatio + weightedLectureMinutes);
            list.add(sum);
        }
        return list;
    }

    @Override
    public String createGPTString(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append("You're a student career consultant and student career counselor." +
                "Next, you'll see information about the courses a student has taken, along with various statistics, such as quiz scores and attention span in those courses." +
                "Based on these statistics, I can make career recommendations based on the student's interests and aptitudes." +
                "Food biotech, math teacher, software developer, etc.").append("\n");

        //studentName은 바뀔 수 있기 때문에, 이름을 바꿨을 경우 gpt 데이터 누락 발생가능. studentId로 바꿨습니다. 주석은 나중에 지울게요
//        List<GPTDataByLecture> gptDataByLectures = gptDataByLectureMongoRepository.findAllByStudentName(student.getName());
        List<GPTDataByLecture> gptDataByLectures = gptDataByLectureMongoRepository.findAllByStudentId(student.getId());

        if (gptDataByLectures.isEmpty())
            throw new IllegalArgumentException("학생 " + student.getName() + "의 GPT 데이터가 존재하지 않습니다.");

        for (GPTDataByLecture gptDataByLecture : gptDataByLectures) {
            sb.append("Lecture title: ").append(gptDataByLecture.getLectureTitle());
            sb.append(" Lecture Category: ").append(gptDataByLecture.getCategory()).append(" Subcategory: ").append(gptDataByLecture.getSubCategory());
            sb.append(" Quiz score: ").append(gptDataByLecture.getTotalQuizScore()).append("/").append(gptDataByLecture.getQuizCount() * 100);
            sb.append(" Lecture Focus: ").append(gptDataByLecture.getFocusRatio()).append("%");
            sb.append("\n");
        }
        sb.append("in korean");
        return sb.toString();
    }

    @Override
    public List<LectureStatisticsByAllStudent> getLectureStatisticsByAllStudents(Integer lectureId) {
        return lectureStatisticMongoRepository.findAllByLectureId(lectureId);
    }


    @Override
    @Transactional
    public void createStatisticsByFinalLecture(Lecture lecture) {
        List<StudentLecture> allStudentInfoByLectureId = studentLectureQueryRepository.findAllStudentInfoByLectureId(lecture.getId());
        createRadarChartStatistics(lecture.getCurriculum(), allStudentInfoByLectureId);
        createLectureStatisticsByAllStudent(lecture, allStudentInfoByLectureId);
        createGPTData(lecture, allStudentInfoByLectureId);
    }

//    Todo: 이 부분에 대해서 트래픽 처리 해줘야함
    @Transactional
    public void createRadarChartStatistics(Curriculum curriculum, List<StudentLecture> allStudentInfoByLectureId) {
        for (StudentLecture studentLecture : allStudentInfoByLectureId) {
            Integer studentId = studentLecture.getStudent().getId();
            radarChartStatisticRepository.findById(studentId)
                    .ifPresentOrElse(
                            radarChartStatistic -> {
                                radarChartStatistic.addStatistic(curriculum, studentLecture);
                            },
                            () -> {
                                RadarChartStatistic newRadarChartStatistic = RadarChartStatistic.of(studentId);
                                newRadarChartStatistic.addStatistic(curriculum, studentLecture);
                                radarChartStatisticRepository.save(newRadarChartStatistic);
                            }
                    );
        }
    }


    // 음.. 먼진 모르겠지만 protected 써야지 Trancsacion 사용가능했음.
    @Transactional
    public void createLectureStatisticsByAllStudent(Lecture lecture, List<StudentLecture> allStudentInfoByLectureId) {
        List<LectureStatisticsByAllStudent> lectureStatisticsByAllStudents = lectureStatisticMongoRepository.findAllByLectureId(lecture.getId());
        if (!lectureStatisticsByAllStudents.isEmpty()) {
            lectureStatisticMongoRepository.deleteAll(lectureStatisticsByAllStudents);
        }
        lectureStatisticMongoRepository.save(LectureStatisticsByAllStudent.of(lecture, allStudentInfoByLectureId));
    }

    @Transactional
    public void createGPTData(Lecture lecture, List<StudentLecture> allStudentInfoByLectureId) {
        Curriculum curriculum = curriculumRepository.findByLecturesContaining(lecture)
                .orElseThrow(() -> new IllegalStateException("curriculum not found"));

        for (StudentLecture studentLecture : allStudentInfoByLectureId) {
            List<GPTDataByLecture> allByStudentNameAndLectures = gptDataByLectureMongoRepository.findAllByStudentIdAndLectureId(studentLecture.getStudent().getId(), lecture.getId());
            if (!allByStudentNameAndLectures.isEmpty()){
                gptDataByLectureMongoRepository.deleteAll(allByStudentNameAndLectures);
            }
            gptDataByLectureMongoRepository.save(GPTDataByLecture.of(lecture, curriculum, studentLecture));
        }
    }
}

//        double[] listAvgFocusRatio = new double[NUMBER_OF_CATEGORIES];
//        int[] listLectureMinutes = new int[NUMBER_OF_CATEGORIES];
//
//        double maxFocusRatio = -1;
//        int maxLectureMinutes = -1;
//
//        int[] retStatistics = new int[NUMBER_OF_CATEGORIES];

//        // 가장 큰 값 찾는 로직 이구나!!!
//        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
//            if (maxFocusRatio > listAvgFocusRatio[i]) {
//                maxFocusRatio = listAvgFocusRatio[i];
//            }
//            if (maxLectureMinutes > listLectureMinutes[i]) {
//                maxLectureMinutes = listLectureMinutes[i];
//            }
//        }

        /* 각 focus_ratio(집중도)에 가중치를 반영하기 위해 필요한 요소.
           NUM_OF_CATEGORIES(현행 7)개의 카테고리 중 가장 높은 focus_ratio가 만점.
           가장 높은 focus_ratio가 WEIGHT_FOCUS_RATIO(현행 15)점.
           (WEIGHT_FOCUS_RATIO/maxFocusRatio)로 계산된 값을
           NUM_OF_CATEGORIES개의 focus_ratio에 각각 곱.
            - Also applies to 'factorWeightingLectureMinutes'
           focus_ratio의 최대값과 lecture_minutes의 최대값이 다를 수 있음.
           이 경우 100.00이 나오지 않을 수 있음.
         */


//        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
//            retStatistics[i] = (int) ((listAvgFocusRatio[i] * factorWeightingFocusRatio) + (listLectureMinutes[i] * factorWeightingLectureMinutes));
//        }

// 카테고리가 현행 7개 이므로 하드코딩(...)
//        statisticsDto.setItem1(retStatistics[0]);
//        statisticsDto.setItem2(retStatistics[1]);
//        statisticsDto.setItem3(retStatistics[2]);
//        statisticsDto.setItem4(retStatistics[3]);
//        statisticsDto.setItem5(retStatistics[4]);
//        statisticsDto.setItem6(retStatistics[5]);
//        statisticsDto.setItem7(retStatistics[6]);
//
//        radarChartStatisticRepository.save(statistics);
//
//        return statisticsDto;
