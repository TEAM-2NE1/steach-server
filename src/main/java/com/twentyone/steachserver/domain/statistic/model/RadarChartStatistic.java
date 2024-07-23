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

    @Column(name = "gpt_career_suggestion", length = 255)
    private String gptCareerSuggestion;

    @Column(name = "average_focus_ratio1", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio1;

    @Column(name = "lecture_count1")
    private Short lectureCount1;

    @Column(name = "sum_lecture_minutes1")
    private Integer sumLectureMinutes1;

    @Column(name = "average_focus_ratio2", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio2;

    @Column(name = "lecture_count2")
    private Short lectureCount2;

    @Column(name = "sum_lecture_minutes2")
    private Integer sumLectureMinutes2;

    @Column(name = "average_focus_ratio3", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio3;

    @Column(name = "lecture_count3")
    private Short lectureCount3;

    @Column(name = "sum_lecture_minutes3")
    private Integer sumLectureMinutes3;

    @Column(name = "average_focus_ratio4", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio4;

    @Column(name = "lecture_count4")
    private Short lectureCount4;

    @Column(name = "sum_lecture_minutes4")
    private Integer sumLectureMinutes4;

    @Column(name = "average_focus_ratio5", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio5;

    @Column(name = "lecture_count5")
    private Short lectureCount5;

    @Column(name = "sum_lecture_minutes5")
    private Integer sumLectureMinutes5;

    @Column(name = "average_focus_ratio6", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio6;

    @Column(name = "lecture_count6")
    private Short lectureCount6;

    @Column(name = "sum_lecture_minutes6")
    private Integer sumLectureMinutes6;

    @Column(name = "average_focus_ratio7", precision = 5, scale = 2)
    private BigDecimal averageFocusRatio7;

    @Column(name = "lecture_count7")
    private Short lectureCount7;

    @Column(name = "sum_lecture_minutes7")
    private Integer sumLectureMinutes7;

    private RadarChartStatistic(Integer studentId) {
        this.id = studentId;
    }

    public static RadarChartStatistic of(Integer studentId) {
        return new RadarChartStatistic(studentId);
    }
    
    public List<StatisticsByCurriculumCategory> getItems() {
        List<StatisticsByCurriculumCategory> items = new ArrayList<>();
        items.add(new StatisticsByCurriculumCategory(averageFocusRatio1, lectureCount1, sumLectureMinutes1));
        items.add(new StatisticsByCurriculumCategory(averageFocusRatio2, lectureCount2, sumLectureMinutes2));
        items.add(new StatisticsByCurriculumCategory(averageFocusRatio3, lectureCount3, sumLectureMinutes3));
        items.add(new StatisticsByCurriculumCategory(averageFocusRatio4, lectureCount4, sumLectureMinutes4));
        items.add(new StatisticsByCurriculumCategory(averageFocusRatio5, lectureCount5, sumLectureMinutes5));
        items.add(new StatisticsByCurriculumCategory(averageFocusRatio6, lectureCount6, sumLectureMinutes6));
        items.add(new StatisticsByCurriculumCategory(averageFocusRatio7, lectureCount7, sumLectureMinutes7));
        return items;
    } 

    public void addStatistic(Curriculum curriculum, StudentLecture studentLecture) {
        String inputCategoryName = curriculum.getCategory().name();
        List<CurriculumCategory> categories = CurriculumCategory.getCategories();
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
                this.averageFocusRatio1 = this.averageFocusRatio1.add(studentLecture.getFocusRatio());
                this.lectureCount1++;
                this.sumLectureMinutes1 += studentLecture.getFocusTime();
            }
            case 2 -> {
                this.averageFocusRatio2 = this.averageFocusRatio2.add(studentLecture.getFocusRatio());
                this.lectureCount2++;
                this.sumLectureMinutes2 += studentLecture.getFocusTime();
            }
            case 3 -> {
                this.averageFocusRatio3 = this.averageFocusRatio3.add(studentLecture.getFocusRatio());
                this.lectureCount3++;
                this.sumLectureMinutes3 += studentLecture.getFocusTime();
            }
            case 4 -> {
                this.averageFocusRatio4 = this.averageFocusRatio4.add(studentLecture.getFocusRatio());
                this.lectureCount4++;
                this.sumLectureMinutes4 += studentLecture.getFocusTime();
            }
            case 5 -> {
                this.averageFocusRatio5 = this.averageFocusRatio5.add(studentLecture.getFocusRatio());
                this.lectureCount5++;
                this.sumLectureMinutes5 += studentLecture.getFocusTime();
            }
            case 6 -> {
                this.averageFocusRatio6 = this.averageFocusRatio6.add(studentLecture.getFocusRatio());
                this.lectureCount6++;
                this.sumLectureMinutes6 += studentLecture.getFocusTime();
            }
            case 7 -> {
                this.averageFocusRatio7 = this.averageFocusRatio7.add(studentLecture.getFocusRatio());
                this.lectureCount7++;
                this.sumLectureMinutes7 += studentLecture.getFocusTime();
            }
            default -> throw new IllegalArgumentException("Invalid category index: " + categoryNum);
        }
    }
}
