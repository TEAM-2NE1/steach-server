package com.twentyone.steachserver.domain.lecture.model;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;
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

    @Column(name = "lecture_order", nullable = false)
    private Integer lectureOrder;

    @Column(name = "lecture_start_time", nullable = false)
    private LocalDateTime lectureStartTime; //시작 날짜로 해석하겠음 - 주효림

    @Column(name = "real_start_time")
    private LocalTime realStartTime;

    @Column(name = "real_end_time")
    private LocalTime realEndTime;

    @Column(name = "number_of_quizzes")
    private Integer numberOfQuizzes = 0;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false, referencedColumnName = "id")
    private Curriculum curriculum;

    @OneToMany(mappedBy = "lecture")
    private List<LectureStudent> lectureStudents = new ArrayList<>();

    public static Lecture of(String title, Integer lectureOrder, LocalDateTime lectureStartTime, LocalTime realStartTime, LocalTime realEndTime, Curriculum curriculum) {
        Lecture lecture = new Lecture();
        lecture.title = title;
        lecture.lectureOrder = lectureOrder;
        lecture.lectureStartTime = lectureStartTime;
        lecture.realStartTime = realStartTime;
        lecture.realEndTime = realEndTime;
        lecture.curriculum = curriculum;

        return lecture;
    }
}
