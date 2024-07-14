package com.twentyone.steachserver.domain.lecture;


import com.twentyone.steachserver.domain.Curricula;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

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
