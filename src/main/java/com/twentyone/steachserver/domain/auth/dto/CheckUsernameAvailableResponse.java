package com.twentyone.steachserver.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckUsernameAvailableResponse {
    //TODO 재활용이 많으니 이름을 변경하자!
    private boolean canUse;
}
