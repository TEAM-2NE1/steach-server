package com.twentyone.steachserver.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curricula_informations")
@NoArgsConstructor
public class CurriculaInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String subTitle;
    private String intro;
    private String target;
    private String requirement;
    private String information;
    private String field;
}
