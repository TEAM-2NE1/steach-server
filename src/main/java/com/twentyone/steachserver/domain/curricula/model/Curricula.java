package com.twentyone.steachserver.domain.curricula.model;

import com.twentyone.steachserver.domain.enums.CurriculaCategory;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.studentCurricula.model.StudentsCurricula;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "curricula")
@NoArgsConstructor
public class Curricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private CurriculaCategory category = CurriculaCategory.ETC;

    @OneToOne
    @JoinColumn(name = "information_id", referencedColumnName = "id", nullable = false)
    private CurriculaInformation information;

    @OneToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    private CurriculaSchedule schedule;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "curricula")
    private Set<StudentsCurricula> studentsCurricula = new HashSet<>();
}
