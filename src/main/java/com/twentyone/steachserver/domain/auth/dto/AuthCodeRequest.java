package com.twentyone.steachserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthCodeRequest {
    //1이상 몇 미만
    private Integer numberOfCode;
}
