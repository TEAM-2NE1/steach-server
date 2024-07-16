package com.twentyone.steachserver.domain.classroom.dto;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(value = AccessLevel.PUBLIC)
public class FocusTimeRequestDto {
    private Integer focusTime;
}
