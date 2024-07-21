package com.twentyone.steachserver.domain.studentLecture.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.statistic.repository.LectureStatisticMongoRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentLectureServiceImpl implements StudentLectureService {

    private final StudentLectureRepository studentLectureRepository;
    private final LectureStatisticMongoRepository studentLectureMongoRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime) {
        studentLectureRepository.findByStudentIdAndLectureId(studentId, lectureId)
                .ifPresentOrElse(
                        studentLecture -> studentLecture.sumFocusTime(focusTime),
                        () -> createAndSaveStudentLecture(studentId, lectureId, focusTime)
                );
    }

    private void createAndSaveStudentLecture(Integer studentId, Integer lectureId, Integer focusTime) {
        Lecture lecture = lectureRepository.getReferenceById(lectureId);
        Student student = studentRepository.getReferenceById(studentId);
        StudentLecture studentLecture = StudentLecture.createStudentLecture(student, lecture, focusTime);
        studentLectureRepository.save(studentLecture);
    }

    @Override
    @Transactional
    public void updateStudentLectureByFinishLecture(Integer lectureId) {
        studentLectureQueryRepository.updateStudentLectureByFinishLecture(lectureId);
    }
}
