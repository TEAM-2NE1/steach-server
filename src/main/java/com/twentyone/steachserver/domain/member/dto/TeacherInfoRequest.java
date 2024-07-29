package com.twentyone.steachserver.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInfoRequest {
    private String nickname;
    private String email;
    private String password;
    private String briefIntroduction;
    private String academicBackground;
    private String specialization;
    private String passwordAuthToken;
}
