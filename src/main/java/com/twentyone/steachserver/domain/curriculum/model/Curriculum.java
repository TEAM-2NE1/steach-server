package com.twentyone.steachserver.domain.curriculum.model;

import com.twentyone.steachserver.config.domain.BaseTimeEntity;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curricula")
@NoArgsConstructor
@Getter(value = AccessLevel.PUBLIC)
@Inheritance(strategy = InheritanceType.JOINED)
public class Curriculum extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    private CurriculumCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "curriculum")
    private List<Lecture> lectures = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER) //항상 필요한 정보들이 detail에 들어가있음(생성날짜 등)
    @JoinColumn(name = "curriculum_detail_id")
    private CurriculumDetail curriculumDetail;

    @OneToMany(mappedBy = "curriculum")
    private List<StudentCurriculum> studentCurricula = new ArrayList<>();

    public static Curriculum of(String title, CurriculumCategory category, Teacher teacher, CurriculumDetail curriculumDetail) {
        Curriculum curriculum = new Curriculum();
        curriculum.title = title;
        curriculum.category = category;
        curriculum.teacher = teacher;
        curriculum.curriculumDetail = curriculumDetail;
        return curriculum;
    }

    public void register() {
        this.curriculumDetail.register();
    }

    public void update(
            String title, String subTitle, String intro, String information, CurriculumCategory category,
            String subCategory, String bannerImgUrl, LocalDate startDate, LocalDate endDate,
            String weekdaysBitmask, LocalTime lectureStartTime, LocalTime lectureEndTime, int maxAttendees) {
        CurriculumDetail detail = this.getCurriculumDetail();

        this.title = title;
        this.category = category;

        detail.update(
                subTitle, intro, information, subCategory, bannerImgUrl, startDate, endDate, weekdaysBitmask,
                lectureStartTime, lectureEndTime, maxAttendees
        );
    }

    public void addStudentCurriculum(StudentCurriculum studentCurriculum) {
        this.studentCurricula.add(studentCurriculum);
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
    }
}