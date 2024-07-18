package com.twentyone.steachserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
{
 username: noob123,
 password: notnoob11,
 name: 귀여운라이언,
 auth_code: 11ADSF2D2S,
 email: gumi123@gmail.com,
}
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeacherSignUpDto {
    private String username;
    private String password;
    private String name;
    private String email;
}
