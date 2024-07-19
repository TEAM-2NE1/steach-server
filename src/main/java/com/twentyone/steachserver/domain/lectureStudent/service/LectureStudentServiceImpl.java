package com.twentyone.steachserver.domain.lectureStudent.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;
import com.twentyone.steachserver.domain.lectureStudent.repository.LectureStudentRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureStudentServiceImpl implements LectureStudentService {

    private final LectureStudentRepository lectureStudentRepository;

    private final StudentService studentService;
    private final LectureService lectureService;

    @Override
    public Optional<LectureStudent> findByStudentIdAndLectureId(Integer studentId, Integer lectureId) {
        return lectureStudentRepository.findByStudentIdAndLectureId(studentId, lectureId);
    }

    @Override
    public void createAndSaveLectureStudent(Integer studentId, Integer lectureId, Integer focusTime) {
        LectureStudent lectureStudent = createLectureStudent(studentId, lectureId, focusTime);

        lectureStudentRepository.save(lectureStudent);
    }

    public LectureStudent createLectureStudent(Integer studentId, Integer lectureId, Integer focusTime) {
        Optional<Student> student = studentService.findStudentById(studentId);
        Optional<Lecture> lecture = lectureService.findLectureById(lectureId);





        if (student.isEmpty()) {
            throw new IllegalArgumentException("student is not exist");
        }
        if (lecture.isEmpty()) {
            throw new IllegalArgumentException("lecture is not exist");
        }

        return LectureStudent.createLectureStudent(student.get(), lecture.get(), focusTime);
    }
}
