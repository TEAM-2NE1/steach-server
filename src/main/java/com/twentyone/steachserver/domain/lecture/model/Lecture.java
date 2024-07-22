package com.twentyone.steachserver.domain.lecture.model;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.dto.update.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
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

    @Column(name = "lecture_order", nullable = false, columnDefinition = "TINYINT(4)")
    private Integer lectureOrder;

    @Column(name = "lecture_start_date", nullable = false)
    private LocalDateTime lectureStartDate; //시작 날짜로 해석하겠음 - 주효림

    @Column(name = "real_start_time")
    private LocalDateTime realStartTime;

    @Column(name = "real_end_time")
    private LocalDateTime realEndTime;

    @Column(name = "number_of_quizzes", columnDefinition = "TINYINT(4)")
    private Integer numberOfQuizzes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false, referencedColumnName = "id")
    private Curriculum curriculum;

    @OneToMany(mappedBy = "lecture")
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "lecture")
    private List<StudentLecture> studentLectures = new ArrayList<>();

    public static Lecture of(String title, Integer lectureOrder, LocalDateTime lectureStartTime, LocalDateTime realStartTime, LocalDateTime realEndTime, Curriculum curriculum) {
        Lecture lecture = new Lecture();
        lecture.title = title;
        lecture.lectureOrder = lectureOrder;
        lecture.lectureStartDate = lectureStartTime;
        lecture.realStartTime = realStartTime;
        lecture.realEndTime = realEndTime;
        lecture.curriculum = curriculum;

        return lecture;
    }


    public void addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
    }

    public void updateRealEndTimeWithNow() {
        this.realEndTime = LocalDateTime.now();
    }

    public void update(UpdateLectureRequestDto lectureRequestDto) {
        this.lectureOrder = Integer.valueOf(lectureRequestDto.lectureOrder());
        this.title = lectureRequestDto.lectureTitle();
        this.lectureStartDate = lectureRequestDto.lectureStartTime();
    }
}
