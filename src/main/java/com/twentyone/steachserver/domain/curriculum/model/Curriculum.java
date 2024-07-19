package com.twentyone.steachserver.domain.curriculum.model;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
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
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "curriculum")
    private List<Lecture> lectures;


    @OneToMany(mappedBy = "curriculum")
    private List<StudentCurriculum> studentCurricula;


    @Enumerated(EnumType.STRING)
    private CurriculumCategory category;


}
