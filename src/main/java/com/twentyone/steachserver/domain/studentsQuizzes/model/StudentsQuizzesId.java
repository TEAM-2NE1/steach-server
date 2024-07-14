package com.twentyone.steachserver.domain.studentsQuizzes.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class StudentsQuizzesId implements Serializable {

    private Integer studentId;
    private Integer quizId;

    public static StudentsQuizzesId createStudentsQuizzesId(Integer studentId, Integer quizId) {
        StudentsQuizzesId studentsQuizzesId = new StudentsQuizzesId();
        studentsQuizzesId.studentId = studentId;
        studentsQuizzesId.quizId = quizId;
        return studentsQuizzesId;
    }
}
