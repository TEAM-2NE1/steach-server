package com.twentyone.steachserver.domain.member.dto;

import com.twentyone.steachserver.domain.member.model.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TeacherInfoResponse extends MemberInfoResponse {
    private Integer volunteerTime;
    private String briefIntroduction;
    private String academicBackground;
    private String specialization;

    public static TeacherInfoResponse fromDomain(Teacher teacher) {
        TeacherInfoResponse response = new TeacherInfoResponse();
        response.username = teacher.getUsername();
        response.nickname = teacher.getName();
        response.email = teacher.getEmail();
        response.volunteerTime = teacher.getVolunteerTime();
        response.briefIntroduction = teacher.getBriefIntroduction();
        response.academicBackground = teacher.getAcademicBackground();
        response.specialization = teacher.getSpecialization();

        return response;
    }

    public static TeacherInfoResponse fromDomainForAll(Teacher teacher) {
        TeacherInfoResponse response = new TeacherInfoResponse();
        response.nickname = teacher.getName();
        response.briefIntroduction = teacher.getBriefIntroduction();
        response.academicBackground = teacher.getAcademicBackground();
        response.specialization = teacher.getSpecialization();

        return response;
    }
}
