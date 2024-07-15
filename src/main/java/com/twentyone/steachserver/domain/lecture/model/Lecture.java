package com.twentyone.steachserver.domain.lecture.model;

import com.twentyone.steachserver.domain.curricula.model.Curricula;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "lecture_order", nullable = false)
    private Integer lectureOrder;

    @Column(length = 255, nullable = false)
    private String title = "";

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false, referencedColumnName = "id")
    private Curricula curricula;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

//    @OneToMany(mappedBy = "lecture")
//    private Set<LecturesStudents> lecturesStudents = new HashSet<>();
}
