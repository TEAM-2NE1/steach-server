package com.twentyone.steachserver.domain.lecture.validator;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LectureValidator {
    public void validateQuizOfLectureAuth(Quiz quiz, Student student) {
        Integer lectureId = quiz.getLecture().getId();
        List<StudentLecture> studentLectures = student.getStudentLectures();
        for (StudentLecture studentLecture : studentLectures) {
            if(lectureId.equals(studentLecture.getLecture().getId())) {
                return;
            }
        }
        throw new IllegalArgumentException("이 학생은 해당 퀴즈가 포함된 강의에 대한 권한이 없습니다.");
    }

    public void validateLectureOfLectureAuth(Lecture lecture, Student student) {
        Integer lectureId = lecture.getId();
        List<StudentLecture> studentLectures = student.getStudentLectures();
        for (StudentLecture studentLecture : studentLectures) {
            if(lectureId.equals(studentLecture.getLecture().getId())) {
                return;
            }
        }
        throw new IllegalArgumentException("이 학생은 해당 강의에 대한 권한이 없습니다.");
    }

    public void validateFinishLecture(Lecture lecture) {
        if(lecture.getRealEndTime() != null && LocalDateTime.now().isAfter(lecture.getRealEndTime())) {
            throw new IllegalArgumentException("이미 종료된 강의입니다.");
        }
    }
}
