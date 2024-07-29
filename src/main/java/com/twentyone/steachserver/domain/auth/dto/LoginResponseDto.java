package com.twentyone.steachserver.domain.auth.dto;

import com.twentyone.steachserver.domain.member.dto.MemberInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto extends MemberInfoResponse {
    private String token;
    private Role role;

    public static LoginResponseDto of(String token, Role role, String username, String nickname, String email) {
        LoginResponseDto dto = new LoginResponseDto();

        dto.token = token;
        dto.role = role;
        dto.username = username;
        dto.nickname = nickname;
        dto.email = email;

        return dto;
    }
}
