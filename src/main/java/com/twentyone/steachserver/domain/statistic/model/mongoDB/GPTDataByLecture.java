package com.twentyone.steachserver.domain.statistic.model.mongoDB;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.statistic.dto.temp.LectureStatisticsByStudentDto;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Document(collection = "gpt_data_by_lecture")
@Getter
public class GPTDataByLecture {
    @Id
    private String id;
    private final Integer studentId;
    private final String studentName;
    private final Integer lectureId;
    private final String curriculumTitle;
    private final CurriculumCategory category;
    private final String lectureName;
    private Integer averageQuizTotalScore;
    private Integer averageQuizAnswerCount;
    private Integer averageFocusTime;
    private BigDecimal averageFocusRatio;

    // Constructor
    private GPTDataByLecture(Integer studentId, String studentName, Lecture lecture, Curriculum curriculum){
        this.studentId = studentId;
        this.studentName = studentName;
        this.lectureId = lecture.getId();
        this.curriculumTitle = curriculum.getTitle();
        this.lectureName = lecture.getTitle();
        this.category = curriculum.getCategory();
    }

    public static GPTDataByLecture of(Lecture lecture, Curriculum curriculum, LectureStatisticsByStudentDto lectureStatisticsByStudent) {
        Integer studentId = lectureStatisticsByStudent.studentId();
        String studentName = lectureStatisticsByStudent.studentName();
        GPTDataByLecture gptDataByLecture = new GPTDataByLecture(studentId, studentName, lecture, curriculum);
        calculateAverages(gptDataByLecture, lectureStatisticsByStudent);
        return gptDataByLecture;
    }

    public static void calculateAverages(GPTDataByLecture gptDataByLecture, LectureStatisticsByStudentDto lectureStatisticsByStudent) {
        Integer quizCount = lectureStatisticsByStudent.quizCount();
        Integer totalQuizTotalScore = lectureStatisticsByStudent.totalQuizTotalScore();
        Integer totalQuizAnswerCount = lectureStatisticsByStudent.totalQuizAnswerCount();
        Integer totalFocusTime = lectureStatisticsByStudent.totalFocusTime();
        BigDecimal totalFocusRatio = lectureStatisticsByStudent.totalFocusRatio();

        if (quizCount > 0) {
            gptDataByLecture.averageFocusTime = totalFocusTime / quizCount;
            gptDataByLecture.averageFocusRatio = totalFocusRatio
                    .divide(BigDecimal.valueOf(quizCount), 2, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            gptDataByLecture.averageQuizAnswerCount = totalQuizAnswerCount / quizCount;
            gptDataByLecture.averageQuizTotalScore = totalQuizTotalScore / quizCount;
        } else {
            gptDataByLecture.averageFocusTime = 0;
            gptDataByLecture.averageFocusRatio = BigDecimal.ZERO;
            gptDataByLecture.averageQuizAnswerCount = 0;
            gptDataByLecture.averageQuizTotalScore = 0;
        }
    }
}
