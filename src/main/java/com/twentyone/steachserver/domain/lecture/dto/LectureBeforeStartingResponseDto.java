package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailByLectureDto;
import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumByLectureDto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LectureBeforeStartingResponseDto {
    private Integer lectureOrder;
    private String lectureTitle = "";
    private LocalDateTime lectureStartTime;

    private SimpleCurriculumByLectureDto curriculumInfo;
    private CurriculumDetailByLectureDto curriculumDetailInfo;

    private List<StudentByLectureDto> students = new ArrayList<>();

    private List<Quiz> quizzes = new ArrayList<>();
    private Integer numberOfQuizzes;


    public LectureBeforeStartingResponseDto(Lecture lecture, SimpleCurriculumByLectureDto curriculumInfo,
                                            CurriculumDetailByLectureDto curriculumDetailInfo,
                                            List<StudentByLectureDto> studentDto) {
        this.lectureOrder = lecture.getLectureOrder();
        this.lectureTitle = lecture.getTitle();
        this.lectureStartTime = lecture.getLectureStartTime();
        this.curriculumInfo = curriculumInfo;
        this.curriculumDetailInfo = curriculumDetailInfo;

        this.quizzes = lecture.getQuizzes();
        this.numberOfQuizzes = lecture.getQuizzes().size();

        this.students = studentDto;
    }
}
