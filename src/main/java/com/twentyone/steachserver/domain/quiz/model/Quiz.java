package com.twentyone.steachserver.domain.quiz.model;

import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quizzes")
@NoArgsConstructor
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 강의의 몇번째 퀴즈인지
    private Integer quizNumber;

    @Column(name = "question", nullable = false)
    private String question;

    @ManyToOne
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    private Lecture lecture;

    @OneToMany(mappedBy = "quiz")
    private Set<StudentQuiz> studentQuizzes = new HashSet<>();

    @OneToMany(mappedBy = "quiz")
    private Set<QuizChoice> quizChoices = new HashSet<>();

    public static Quiz createQuiz(QuizRequestDto request, Lecture lecture) {
        Quiz quiz = new Quiz();
        quiz.setLecture(lecture);
        quiz.setQuestion(request.getQuestion());
        quiz.setQuizNumber(request.getQuizNumber());
        return quiz;
    }

    public void addStudentQuiz(StudentQuiz studentQuiz) {
        this.getStudentQuizzes().add(studentQuiz);
        studentQuiz.updateQuiz(this);
    }

    public void addChoice(QuizChoice quizChoice) {
        this.getQuizChoices().add(quizChoice);
        quizChoice.updateQuiz(this);
    }
}