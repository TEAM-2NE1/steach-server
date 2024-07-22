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
    private List<Lecture> lectures;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_detail_id")
    private CurriculumDetail curriculumDetail;

    @OneToMany(mappedBy = "curriculum")
    private List<StudentCurriculum> studentCurricula;

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
}