package com.twentyone.steachserver.domain.statistic.model;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.statistic.dto.StatisticsByCurriculumCategory;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "statistics")
@NoArgsConstructor
@Getter(value = AccessLevel.PRIVATE)
@Setter(value = AccessLevel.PRIVATE)
public class RadarChartStatistic {
    @Id
    @Column(name = "student_id")
    private Integer id;

    // 255자 이하로 넣어주세요/
    @Column(name = "gpt_career_suggestion", length = 255)
    private String gptCareerSuggestion = "";

    @Column(name = "average_focus_ratio1", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio1 = new BigDecimal(0);

    @Column(name = "lecture_count1")
    private Short lectureCount1 = 0;

    @Column(name = "sum_lecture_minutes1")
    private Integer sumLectureMinutes1 = 0;

    @Column(name = "average_focus_ratio2", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio2 = new BigDecimal(0);

    @Column(name = "lecture_count2")
    private Short lectureCount2 = 0;

    @Column(name = "sum_lecture_minutes2")
    private Integer sumLectureMinutes2 = 0;

    @Column(name = "average_focus_ratio3", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio3 = new BigDecimal(0);

    @Column(name = "lecture_count3")
    private Short lectureCount3 = 0;

    @Column(name = "sum_lecture_minutes3")
    private Integer sumLectureMinutes3 = 0;

    @Column(name = "average_focus_ratio4", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio4 = new BigDecimal(0);

    @Column(name = "lecture_count4")
    private Short lectureCount4 = 0;

    @Column(name = "sum_lecture_minutes4")
    private Integer sumLectureMinutes4 = 0;

    @Column(name = "average_focus_ratio5", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio5 = new BigDecimal(0);

    @Column(name = "lecture_count5")
    private Short lectureCount5 = 0;

    @Column(name = "sum_lecture_minutes5")
    private Integer sumLectureMinutes5 = 0;

    @Column(name = "average_focus_ratio6", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio6 = new BigDecimal(0);

    @Column(name = "lecture_count6")
    private Short lectureCount6 = 0;

    @Column(name = "sum_lecture_minutes6")
    private Integer sumLectureMinutes6 = 0;

    @Column(name = "average_focus_ratio7", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio7 = new BigDecimal(0);

    @Column(name = "lecture_count7")
    private Short lectureCount7 = 0;

    @Column(name = "sum_lecture_minutes7")
    private Integer sumLectureMinutes7 = 0;

    private RadarChartStatistic(Integer studentId) {
        this.id = studentId;
    }

    public static RadarChartStatistic of(Integer studentId) {
        return new RadarChartStatistic(studentId);
    }

    public List<StatisticsByCurriculumCategory> getItems() {
        List<StatisticsByCurriculumCategory> items = new ArrayList<>();
        add(items, averageFocusRatio1, sumLectureMinutes1);
        add(items, averageFocusRatio2, sumLectureMinutes2);
        add(items, averageFocusRatio3, sumLectureMinutes3);
        add(items, averageFocusRatio4, sumLectureMinutes4);
        add(items, averageFocusRatio5, sumLectureMinutes5);
        add(items, averageFocusRatio6, sumLectureMinutes6);
        add(items, averageFocusRatio7, sumLectureMinutes7);
        return items;
    }

    private void add(List<StatisticsByCurriculumCategory> items, BigDecimal averageFocusRatio, Integer sumLectureMinutes) {
        if (sumLectureMinutes > 0) {
            items.add(new StatisticsByCurriculumCategory(averageFocusRatio, sumLectureMinutes));
//            items.add(new StatisticsByCurriculumCategory(averageFocusRatio.divide(BigDecimal.valueOf(sumLectureMinutes), 2, RoundingMode.HALF_UP), sumLectureMinutes));
        } else {
            items.add(new StatisticsByCurriculumCategory(BigDecimal.ZERO, 0));
        }
    }

    public void addStatistic(Curriculum curriculum, StudentLecture studentLecture) {
        String inputCategoryName = curriculum.getCategory().name();
        List<CurriculumCategory> categories = CurriculumCategory.getCategories();

        // 맞는 카테고리 찾으면 더해줌.
        for (int i = 0; i < categories.size(); i++) {
            String categoryName = CurriculumCategory.getCategoryByIndex(i).name();
            if (inputCategoryName.equals(categoryName)) {
                sumStatistic(i + 1, studentLecture);
                break;
            }
        }
    }

    private void sumStatistic(Integer categoryNum, StudentLecture studentLecture) {
        switch (categoryNum) {
            case 1 -> {
                this.averageFocusRatio1 = this.averageFocusRatio1.add(studentLecture.getFocusRatio()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                this.lectureCount1++;
                this.sumLectureMinutes1 += studentLecture.getFocusTime();
            }
            case 2 -> {
                this.averageFocusRatio2 = this.averageFocusRatio2.add(studentLecture.getFocusRatio()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                this.lectureCount2++;
                this.sumLectureMinutes2 += studentLecture.getFocusTime();
            }
            case 3 -> {
                this.averageFocusRatio3 = this.averageFocusRatio3.add(studentLecture.getFocusRatio()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                this.lectureCount3++;
                this.sumLectureMinutes3 += studentLecture.getFocusTime();
            }
            case 4 -> {
                this.averageFocusRatio4 = this.averageFocusRatio4.add(studentLecture.getFocusRatio()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                this.lectureCount4++;
                this.sumLectureMinutes4 += studentLecture.getFocusTime();
            }
            case 5 -> {
                this.averageFocusRatio5 = this.averageFocusRatio5.add(studentLecture.getFocusRatio()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                this.lectureCount5++;
                this.sumLectureMinutes5 += studentLecture.getFocusTime();
            }
            case 6 -> {
                this.averageFocusRatio6 = this.averageFocusRatio6.add(studentLecture.getFocusRatio()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                this.lectureCount6++;
                this.sumLectureMinutes6 += studentLecture.getFocusTime();
            }
            case 7 -> {
                this.averageFocusRatio7 = this.averageFocusRatio7.add(studentLecture.getFocusRatio()).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                this.lectureCount7++;
                this.sumLectureMinutes7 += studentLecture.getFocusTime();
            }
            default -> throw new IllegalArgumentException("Invalid category index: " + categoryNum);
        }
    }
}
