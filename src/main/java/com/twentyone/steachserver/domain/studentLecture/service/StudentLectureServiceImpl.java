package com.twentyone.steachserver.domain.studentLecture.service;

import com.twentyone.steachserver.domain.lecture.dto.StudentInfoByLectureDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentLectureServiceImpl implements StudentLectureService {

    private final StudentLectureRepository studentLectureRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;


    @Override
    public Optional<StudentLecture> findByStudentIdAndLectureId(Integer studentId, Integer lectureId) {
        return studentLectureRepository.findByStudentIdAndLectureId(studentId, lectureId);
    }

    @Override
    public List<StudentInfoByLectureDto> findAllStudentInfoByLectureId(Integer lectureId) {
        return studentLectureQueryRepository.findAllStudentInfoByLectureId(lectureId);
    }

    @Override
    public void createAndSaveStudentLecture(Student student, Lecture lecture, Integer focusTime) {
        StudentLecture studentLecture = createStudentLecture(student, lecture, focusTime);
        studentLectureRepository.save(studentLecture);
    }

    @Override
    public void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime) {

    }

    public StudentLecture createStudentLecture(Student student, Lecture lecture, Integer focusTime) {
        if (student == null) {
            throw new IllegalArgumentException("student is not exist");
        }
        if (lecture== null) {
            throw new IllegalArgumentException("lecture is not exist");
        }

        return StudentLecture.createStudentLecture(student, lecture, focusTime);
    }
}
