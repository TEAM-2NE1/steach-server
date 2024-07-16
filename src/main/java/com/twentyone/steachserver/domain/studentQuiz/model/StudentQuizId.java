package com.twentyone.steachserver.domain.studentQuiz.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class StudentQuizId implements Serializable {
    private Integer studentId;
    private Integer quizId;

    public static StudentQuizId createStudentQuizId(Integer studentId, Integer quizId) {
        StudentQuizId studentQuizId = new StudentQuizId();
        studentQuizId.studentId = studentId;
        studentQuizId.quizId = quizId;
        return studentQuizId;
    }
}
