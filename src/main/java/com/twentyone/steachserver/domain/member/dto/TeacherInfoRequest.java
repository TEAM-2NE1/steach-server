package com.twentyone.steachserver.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
nickname, email, password는 null로 들어오면 수정되지 않음
briefIntroduction, academicBackground, specilization은 무조건 넣어줘야함. 아니면 null처리됨
passwordAuthToken은 필수로 넣어줘야함
*/
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
    @NotNull
    private String passwordAuthToken;
}
