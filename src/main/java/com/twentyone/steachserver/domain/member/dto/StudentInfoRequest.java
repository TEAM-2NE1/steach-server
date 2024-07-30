package com.twentyone.steachserver.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
nickname, email, password는 null로 들어오면 수정되지 않음
passwordAuthToken은 필수로 넣어줘야함
*/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoRequest {
    private String nickname;
    private String email;
    private String password;

    @NotNull
    private String passwordAuthToken; //수정을 위한 임시토큰 -> check/password API이용
}
