package com.twentyone.steachserver.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoRequest {
    private String name;
    private String email;
    private String tempToken; //수정을 위한 임시토큰 -> check/password API이용
}
