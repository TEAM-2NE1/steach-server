package com.twentyone.steachserver.domain.studentQuiz.dto;

import lombok.Builder;

@Builder
public record StudentQuizRequestDto(Integer score, String studentChoice) {

}
