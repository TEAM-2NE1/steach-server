package com.twentyone.steachserver.domain.lecture.dto;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailByLectureDto;
import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumByLectureDto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.quiz.dto.QuizByLectureDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class LectureBeforeStartingResponseDto extends LectureResponseDto{
    private Boolean isCompleted = false;

    private Integer lectureId;
    private String lectureTitle;
    private Integer lectureOrder;

    private SimpleCurriculumByLectureDto curriculumInfo;
    private CurriculumDetailByLectureDto curriculumDetailInfo;

    private LocalDateTime lectureStartTime;
    private LocalDateTime lectureEndTime;

    private List<StudentByLectureDto> students = new ArrayList<>();

    private List<QuizByLectureDto> quizzes = new ArrayList<>();
    private Integer numberOfQuizzes = 0;

    protected LectureBeforeStartingResponseDto(LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto) {
        this.lectureId = lectureBeforeStartingResponseDto.lectureId;
        this.lectureTitle = lectureBeforeStartingResponseDto.lectureTitle;
        this.lectureOrder = lectureBeforeStartingResponseDto.lectureOrder;
        this.lectureStartTime = lectureBeforeStartingResponseDto.lectureStartTime;
        this.lectureEndTime = lectureBeforeStartingResponseDto.lectureEndTime;
        this.curriculumInfo = lectureBeforeStartingResponseDto.curriculumInfo;
        this.curriculumDetailInfo = lectureBeforeStartingResponseDto.curriculumDetailInfo;
        this.students = lectureBeforeStartingResponseDto.students;
        this.quizzes = lectureBeforeStartingResponseDto.quizzes;
        this.numberOfQuizzes = lectureBeforeStartingResponseDto.numberOfQuizzes;
        this.isCompleted = lectureBeforeStartingResponseDto.isCompleted;
    }
    private LectureBeforeStartingResponseDto(Lecture lecture,
                                             SimpleCurriculumByLectureDto curriculumInfo,
                                             CurriculumDetailByLectureDto curriculumDetailInfo,
                                             List<StudentByLectureDto> studentDtos) {
        this.lectureId = lecture.getId();
        this.lectureTitle = lecture.getTitle();
        this.lectureOrder = lecture.getLectureOrder();
        this.lectureStartTime = lecture.getLectureStartDate();
        this.lectureEndTime = lecture.getLectureStartDate().with(curriculumDetailInfo.lectureCloseTime());

        this.numberOfQuizzes = lecture.getNumberOfQuizzes();

        this.curriculumInfo = curriculumInfo;
        this.curriculumDetailInfo = curriculumDetailInfo;

        List<Quiz> quizList = lecture.getQuizzes();
        for (Quiz quiz : quizList) {
            QuizByLectureDto quizByLectureDto = QuizByLectureDto.of(quiz);
            this.quizzes.add(quizByLectureDto);
        }

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
