package com.twentyone.steachserver.domain.lecture.model;


import com.twentyone.steachserver.domain.curricula.model.Curricula;
import com.twentyone.steachserver.domain.lectureStudents.model.LecturesStudents;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "lectures")
@NoArgsConstructor
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer lectureOrder;
    private String title;

    @ManyToOne
    @JoinColumn(name = "curricula_id")
    private Curricula curricula;

    @OneToMany(mappedBy = "lecture")
    private Set<LecturesStudents> lecturesStudents = new HashSet<>();

}
