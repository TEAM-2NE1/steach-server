package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailByLectureDto;
import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumByLectureDto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.member.dto.StudentByLectureDto;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureBeforeStartingResponseDto extends LectureResponseDto{
    private SimpleCurriculumByLectureDto curriculumInfo;
    private CurriculumDetailByLectureDto curriculumDetailInfo;

    private List<StudentByLectureDto> students = new ArrayList<>();

    private List<Quiz> quizzes = new ArrayList<>();
    private Integer numberOfQuizzes;
    private Boolean isCompleted = false;

    private LectureBeforeStartingResponseDto(Lecture lecture,
                                             SimpleCurriculumByLectureDto curriculumInfo,
                                             CurriculumDetailByLectureDto curriculumDetailInfo,
                                             List<StudentByLectureDto> studentDtos) {
        this.curriculumInfo = curriculumInfo;
        this.curriculumDetailInfo = curriculumDetailInfo;

        this.quizzes = lecture.getQuizzes();
        this.numberOfQuizzes = lecture.getQuizzes().size();

        this.students = studentDtos;
    }

    public static LectureBeforeStartingResponseDto of(Lecture lecture, SimpleCurriculumByLectureDto curriculumInfo,
                        CurriculumDetailByLectureDto curriculumDetailInfo,
                        List<StudentByLectureDto> studentDto) {
        return new LectureBeforeStartingResponseDto(lecture, curriculumInfo, curriculumDetailInfo, studentDto);
    }

    public void completeLecture(){
        this.isCompleted = true;
    }
}
