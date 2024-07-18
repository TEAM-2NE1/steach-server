package com.twentyone.steachserver.domain.member.dto;

import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record StudentByLectureDto(String name, String email) {
    public static StudentByLectureDto createStudentByLectureDto(String name, String email) {
        return new StudentByLectureDto(name, email);
    }
}
