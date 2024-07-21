package com.twentyone.steachserver.domain.statistic.model.mongoDB;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import jakarta.persistence.Id;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "lecture_statistics_by_all_student")
@Getter
public class LectureStatisticsByAllStudent {
    @Id
    private ObjectId id;
    private final Integer lectureId;
    private final Integer averageQuizTotalScore;
    private final Integer averageQuizAnswerCount;
    private final Integer averageFocusTime;
    private final BigDecimal averageFocusRatio;

    private LectureStatisticsByAllStudent(Lecture lecture, Integer averageQuizTotalScore, Integer averageQuizAnswerCount, Integer averageFocusTime, BigDecimal averageFocusRatio) {
        this.lectureId = lecture.getId();
        this.averageQuizTotalScore = averageQuizTotalScore;
        this.averageQuizAnswerCount = averageQuizAnswerCount;
        this.averageFocusTime = averageFocusTime;
        this.averageFocusRatio = averageFocusRatio;
    }

    public static LectureStatisticsByAllStudent of(Lecture lecture, Integer averageQuizTotalScore, Integer averageQuizAnswerCount, Integer averageFocusTime, BigDecimal averageFocusRatio) {
        return new LectureStatisticsByAllStudent(lecture, averageQuizTotalScore, averageQuizAnswerCount, averageFocusTime, averageFocusRatio);
    }


}
