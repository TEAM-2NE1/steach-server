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

    @Column(length = 10000) //varchar(10000)
    private String subTitle;

    @Column(length = 10000) //varchar(10000)
    private String intro;

    @Column(length = 10000) //varchar(10000)
    private String target;

    @Column(length = 10000) //varchar(10000)
    private String requirement;

    @Lob
    @Column(length = 10000000) //mediumtext
    private String information;

    @Column(name = "banner_img_url", length = 1000)
    private String bannerImgUrl;
}
