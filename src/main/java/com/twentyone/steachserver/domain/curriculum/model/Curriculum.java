package com.twentyone.steachserver.domain.curriculum.model;

import com.twentyone.steachserver.domain.enums.CurriculaCategory;
import com.twentyone.steachserver.domain.member.model.Teacher;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curricula")
@NoArgsConstructor
@Getter(value = AccessLevel.PUBLIC)
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    private CurriculaCategory category;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "detail_id")
    private CurriculumDetail curriculumDetail;

    public static Curriculum of(String title, CurriculaCategory category, Teacher teacher, CurriculumDetail curriculumDetail) {
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