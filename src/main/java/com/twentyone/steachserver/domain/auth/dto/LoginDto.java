package com.twentyone.steachserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
{
 username: noob123,
 password: notnoob11,
}
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginDto {
    private String username;
    private String password;
}

