package com.twentyone.steachserver.domain.lecture;


import com.twentyone.steachserver.domain.Curricula;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "lectures")
@NoArgsConstructor
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "curricula_id")
    private Curricula curricula;

    private Integer lectureOrder;
    private String title;
}
