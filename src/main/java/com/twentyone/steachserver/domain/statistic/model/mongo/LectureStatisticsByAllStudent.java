package com.twentyone.steachserver.domain.statistic.model.mongo;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Document(collection = "lecture_statistics_by_all_student")
@Getter
@Setter
@NoArgsConstructor // 인스턴스화 할때 생성자가 있어야함.
public class LectureStatisticsByAllStudent {
    @Id
    private ObjectId id;
    private Integer lectureId;
    private Integer averageQuizTotalScore;
    private Integer averageQuizAnswerCount;
    private Integer averageFocusTime;
    private BigDecimal averageFocusRatio;

    private LectureStatisticsByAllStudent(Lecture lecture, Integer averageQuizTotalScore, Integer averageQuizAnswerCount, Integer averageFocusTime, BigDecimal averageFocusRatio) {
        this.lectureId = lecture.getId();
        this.averageQuizTotalScore = averageQuizTotalScore;
        this.averageQuizAnswerCount = averageQuizAnswerCount;
        this.averageFocusTime = averageFocusTime;
        this.averageFocusRatio = averageFocusRatio;
    }

    private LectureStatisticsByAllStudent(Lecture lecture) {
        this.lectureId = lecture.getId();
    }

    public static LectureStatisticsByAllStudent of(Lecture lecture, List<StudentLecture> allStudentInfoByLectureId) {
        int quizSize = allStudentInfoByLectureId.size();

        LectureStatisticsByAllStudent lectureStatisticsByAllStudent = new LectureStatisticsByAllStudent(lecture);

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
        if (quizSize > 0) {
            lectureStatisticsByAllStudent.averageQuizTotalScore = totalQuizTotalScore / quizSize;
            lectureStatisticsByAllStudent.averageQuizAnswerCount = totalQuizAnswerCount / quizSize;
            lectureStatisticsByAllStudent.averageFocusTime = totalFocusTime / quizSize;
            lectureStatisticsByAllStudent.averageFocusRatio = totalFocusRatio.divide(BigDecimal.valueOf(quizSize), 2, RoundingMode.HALF_UP);
        } else {
            lectureStatisticsByAllStudent.averageQuizTotalScore = 0;
            lectureStatisticsByAllStudent.averageQuizAnswerCount = 0;
            lectureStatisticsByAllStudent.averageFocusTime = 0;
            lectureStatisticsByAllStudent.averageFocusRatio = BigDecimal.ZERO;
        }
        return lectureStatisticsByAllStudent;
    }

}
