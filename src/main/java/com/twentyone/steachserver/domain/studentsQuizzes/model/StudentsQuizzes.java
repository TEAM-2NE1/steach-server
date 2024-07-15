package com.twentyone.steachserver.domain.studentsQuizzes.model;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.*;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "students_quizzes")
public class StudentsQuizzes {
    @EmbeddedId
    private StudentsQuizzesId id;

    @ManyToOne
    @MapsId("studentId") //  엔터티의 외래 키 필드를 포함된 기본 키 클래스의 해당 필드에 매핑합니다.
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    @Column(name = "total_score")
    private Integer totalScore;

    protected StudentsQuizzes() {}

    private StudentsQuizzes(Student student, Quiz quiz) {
        this.id = StudentsQuizzesId.createStudentsQuizzesId(student.getId(), quiz.getId());
        this.student = student;
        this.quiz = quiz;
    }

    public static StudentsQuizzes createStudentsQuizzes(Student student, Quiz quiz) {
        return new StudentsQuizzes(student, quiz);
    }

    public void updateScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public void updateQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
