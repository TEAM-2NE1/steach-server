package com.twentyone.steachserver.domain.statistic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "statistics")
@NoArgsConstructor
@Getter(value = AccessLevel.PRIVATE)
@Setter(value = AccessLevel.PRIVATE)
public class Statistic {

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
}
