package com.twentyone.steachserver.domain.studentsQuizzes;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class StudentsQuizzesId implements Serializable {

    private Long studentId;
    private Long quizId;
}
