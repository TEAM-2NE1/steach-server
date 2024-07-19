package com.twentyone.steachserver.domain.member.dto;

import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizDto;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record StudentByLectureDto(String name, String email) {
    public static StudentByLectureDto createStudentByLectureDto(Student student) {
        return new StudentByLectureDto(student.getName(), student.getEmail());
    }
}
