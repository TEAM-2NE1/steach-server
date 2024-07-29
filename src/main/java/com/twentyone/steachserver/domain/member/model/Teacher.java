package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "teachers")
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id") // 상속받은 엔티티의 기본 키를 지정
public class Teacher extends LoginCredential{

    @Column(length = 30, nullable = false)
    private String name;

    @Column(unique = true)
    private String email = "";

    @Column(nullable = false, columnDefinition = "SMALLINT(6)")
    private Integer volunteerTime = 0;

    @Column(length = 255)
    private String pathQualification;

    private String briefIntroduction; //강사 소개

    private String academicBackground; //주요학력

    private String specialization;

    @OneToMany(mappedBy = "teacher")
    List<Curriculum> curriculumList;

    @Override
    public boolean equals(Object obj) {
        Teacher teacher = (Teacher) obj;

        return teacher.getId().equals(this.getId());
    }

    public static Teacher of(String username, String password, String name, String email, String pathQualification) {
        Teacher teacher = new Teacher();

        teacher.setUsername(username);
        teacher.setPassword(password);
        teacher.name = name;
        teacher.email = email;
        teacher.pathQualification = pathQualification;

        return teacher;
    }

    public void updateInfo(String name, String email, String briefIntroduction, String academicBackground, String specialization, String password) {
        if (name != null && !name.equals("")) {
            this.name = name;
        }

        if (email != null && !email.equals("")) {
            this.email = email;
        }

        if (password != null & !password.equals("")) {
            this.setPassword(password);
        }

        this.briefIntroduction = briefIntroduction;
        this.academicBackground = academicBackground;
        this.specialization = specialization;
    }

    public void updateVolunteerMinute(Integer volunteerTime) {
        this.volunteerTime = volunteerTime;
    }
}
