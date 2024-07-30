package com.twentyone.steachserver.domain.member.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public abstract class MemberInfoResponse {
    protected String username;
    protected String nickname;
    protected String email;
}
