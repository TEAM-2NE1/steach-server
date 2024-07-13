package com.twentyone.steachserver.domain.quiz.model;

import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quizzes")
@NoArgsConstructor
@Getter(value = AccessLevel.PRIVATE)
@Setter(value = AccessLevel.PRIVATE)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lectures_id")
    private Lecture lectures;

    // 강의의 몇번째 퀴즈인지
    private Integer quizNumber;
    private String question;

    public static Quiz createQuiz(QuizRequestDto request, Lecture lecture) {
        Quiz quiz = new Quiz();
        quiz.setLectures(lecture);
        quiz.setQuestion(request.getQuestion());
        quiz.setQuizNumber(request.getQuizNumber());
        return quiz;
    }

    public void updateQuizDetails(Integer quizNumber, String question, Lecture lectures) {
        this.quizNumber = quizNumber;
        this.question = question;
        this.lectures = lectures;
    }
}
