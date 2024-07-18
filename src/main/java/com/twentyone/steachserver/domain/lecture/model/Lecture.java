package com.twentyone.steachserver.domain.lecture.model;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.dto.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)


@Entity
@Table(name = "lectures")
@NoArgsConstructor
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String title = "";

    @Column(name = "lecture_order", nullable = false)
    private Integer lectureOrder;

    @Column(name = "lecture_start_time", nullable = false)
    private LocalDateTime lectureStartTime;

    @Column(name = "real_start_time")
    private LocalDateTime realStartTime;

    @Column(name = "real_end_time")
    private LocalDateTime realEndTime;

    @Column(name = "number_of_quizzes")
    private Integer numberOfQuizzes;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false, referencedColumnName = "id")
    private Curriculum curriculum;

    @OneToMany(mappedBy = "lecture")
    private List<StudentLecture> studentLectures = new ArrayList<>();

    @OneToMany(mappedBy = "lecture")
    private List<Quiz> quizzes = new ArrayList<>();

    public void addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
    }

    public void updateRealEndTimeWithNow() {
        this.realEndTime = LocalDateTime.now();
    }

    public void update(UpdateLectureRequestDto lectureRequestDto) {
        this.lectureOrder = Integer.valueOf(lectureRequestDto.getLectureOrder());
        this.title = lectureRequestDto.getTitle();
        this.lectureStartTime = lectureRequestDto.getLectureStartTime();
    }
}
