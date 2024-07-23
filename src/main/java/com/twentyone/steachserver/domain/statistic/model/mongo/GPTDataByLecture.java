package com.twentyone.steachserver.domain.statistic.model.mongo;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Document(collection = "gpt_data_by_lecture")
@Getter
@Setter
@NoArgsConstructor // 인스턴스화 할때 생성자가 있어야함.
public class GPTDataByLecture {
    @Id
    private String id;
    private String studentName;
    private String curriculumTitle;
    private String lectureTitle;
    private CurriculumCategory category;
    private String subCategory;

    private Integer quizCount;
    private Integer totalQuizScore;
    private Integer QuizAnswerCount;
    private Integer FocusTime;
    private BigDecimal FocusRatio;

    // Constructor
    private GPTDataByLecture(Lecture lecture, Curriculum curriculum, StudentLecture studentLecture) {
        this.studentName = studentLecture.getStudent().getName();
        this.curriculumTitle = curriculum.getTitle();
        this.lectureTitle = lecture.getTitle();
        this.category = curriculum.getCategory();
        this.subCategory = curriculum.getCurriculumDetail().getSubCategory();

        this.quizCount = lecture.getNumberOfQuizzes();
        this.totalQuizScore = studentLecture.getQuizTotalScore();
        this.QuizAnswerCount = studentLecture.getQuizAnswerCount();
        this.FocusTime = studentLecture.getFocusTime();
        this.FocusRatio = studentLecture.getFocusRatio();
    }

    public static GPTDataByLecture of(Lecture lecture, Curriculum curriculum, StudentLecture studentLecture) {
        return new GPTDataByLecture(lecture, curriculum, studentLecture);
    }
}
