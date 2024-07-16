package com.twentyone.steachserver.domain.lectureStudents.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.lectureStudents.model.LecturesStudents;
import com.twentyone.steachserver.domain.lectureStudents.repository.LecturesStudentsRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureStudentsServiceImpl implements LectureStudentsService {

    private final LecturesStudentsRepository lecturesStudentsRepository;

    private final StudentService studentService;
    private final LectureService lectureService;

    @Override
    public Optional<LecturesStudents> findByStudentIdAndLectureId(Integer studentId, Integer lectureId) {
        return lecturesStudentsRepository.findByStudentIdAndLectureId(studentId, lectureId);
    }

    @Override
    public void createAndSaveLecturesStudents(Integer studentId, Integer lectureId, Integer focusTime) {
        LecturesStudents lecturesStudents = createLecturesStudents(studentId, lectureId, focusTime);

        lecturesStudentsRepository.save(lecturesStudents);
    }

    public LecturesStudents createLecturesStudents(Integer studentId, Integer lectureId, Integer focusTime) {
        Optional<Student> student = studentService.findStudentById(studentId);
        Optional<Lecture> lecture = lectureService.findLectureById(lectureId);

        if (student.isEmpty()) {
            throw new IllegalArgumentException("student is not exist");
        }
        if (lecture.isEmpty()) {
            throw new IllegalArgumentException("lecture is not exist");
        }

        return LecturesStudents.createLecturesStudents(student.get(), lecture.get(), focusTime);
    }
}
