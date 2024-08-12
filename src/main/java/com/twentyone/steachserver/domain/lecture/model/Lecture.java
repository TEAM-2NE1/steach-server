package com.twentyone.steachserver.domain.lecture.model;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // 강의가 언제 시작하는 날짜와 커리큘럼의 startTime을 합한 값.
    @Column(name = "lecture_start_date", nullable = false)
    private LocalDateTime lectureStartDate; //시작 날짜로 해석하겠음 - 주효림

    @Column(name = "lecture_end_date", nullable = false)
    private LocalDateTime lectureEndDate;

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

    public static Lecture of(String title, Integer lectureOrder, LocalDateTime lectureStartTime, LocalDateTime lectureEndDate, Curriculum curriculum) {
        Lecture lecture = new Lecture();
        lecture.title = title;
        lecture.lectureOrder = lectureOrder;
        lecture.lectureStartDate = lectureStartTime;
        lecture.lectureEndDate = lectureEndDate;
        lecture.curriculum = curriculum;

        curriculum.addLecture(lecture);
        return lecture;
    }

    public void addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        this.numberOfQuizzes++;
    }

    public void updateRealEndTimeWithNow() {
        this.realEndTime = LocalDateTime.now();
    }
    public void updateRealStartTimeWithNow() {
        System.out.println("update");
        this.realStartTime = LocalDateTime.now();
    }

    public void update(String lectureTitle, LocalTime lectureStartTime, LocalTime lectureEndTime) {
        if (lectureTitle != null) {
            this.title = lectureTitle;
        }

        if (lectureStartTime != null) {
            this.lectureStartDate = this.lectureStartDate.withHour(lectureStartTime.getHour()).withMinute(lectureStartTime.getMinute());
        }

        if (lectureEndDate != null) {
            this.lectureEndDate = this.lectureEndDate.withHour(lectureEndTime.getHour()).withMinute(lectureEndTime.getMinute());
        }
    }

    public void addStudentLecture(StudentLecture studentLecture) {
        this.studentLectures.add(studentLecture);
    }
}