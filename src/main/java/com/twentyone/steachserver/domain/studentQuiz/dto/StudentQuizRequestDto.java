package com.twentyone.steachserver.domain.studentQuiz.dto;

import lombok.Getter;
public record StudentQuizRequestDto(Integer score, String studentChoice, String studentName) {

}
