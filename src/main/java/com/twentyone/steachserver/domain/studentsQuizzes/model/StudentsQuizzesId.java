package com.twentyone.steachserver.domain.studentsQuizzes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter(value = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class StudentsQuizzesId implements Serializable {
    private Integer studentId;
    private Integer quizId;

//    private StudentsQuizzesId(Integer studentId, Integer quizId) {
//        this.studentId = studentId;
//        this.quizId = quizId;
//    }

    public static StudentsQuizzesId createStudentsQuizzesId(Integer studentId, Integer quizId) {
        StudentsQuizzesId studentsQuizzesId = new StudentsQuizzesId();
        studentsQuizzesId.studentId = studentId;
        studentsQuizzesId.quizId = quizId;
        return studentsQuizzesId;
    }
}
