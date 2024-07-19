package com.twentyone.steachserver.domain.studentLecture.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.studentLecture.dto.StudentLectureStatisticDto;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureMongoRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentLectureServiceImpl implements StudentLectureService {

    private final StudentLectureRepository studentLectureRepository;
    private final StudentLectureMongoRepository studentLectureMongoRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    @Override
    public Optional<StudentLecture> findByStudentIdAndLectureId(Integer studentId, Integer lectureId) {
        return studentLectureRepository.findByStudentIdAndLectureId(studentId, lectureId);
    }

    @Override
    @Transactional
    public void saveTimeFocusTime(Integer studentId, Integer lectureId, Integer focusTime) {
        Optional<StudentLecture> byStudentIdAndLectureId = findByStudentIdAndLectureId(studentId, lectureId);

        // 존재하면 더 하고
        if (byStudentIdAndLectureId.isPresent()) {
            StudentLecture studentLecture = byStudentIdAndLectureId.get();
            studentLecture.sumFocusTime(focusTime);
            return;
        }

        // 존재하지 않으면 새로 만들어서 넣어주고
        createAndSaveStudentLecture(studentId, lectureId, focusTime);
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

    @Override
    @Transactional
    public StudentLectureStatisticDto createLectureStudentStatistic(Integer lectureId) {
        List<StudentLecture> allStudentInfoByLectureId = findAllStudentInfoByLectureId(lectureId);

        int studentCount = allStudentInfoByLectureId.size();

        Integer totalQuizTotalScore = 0;
        Integer totalQuizAnswerCount = 0;
        Integer totalFocusTime = 0;
        BigDecimal totalFocusRatio = BigDecimal.valueOf(0);


        for (StudentLecture studentLecture : allStudentInfoByLectureId) {
            totalQuizTotalScore += studentLecture.getQuizTotalScore();
            totalQuizAnswerCount += studentLecture.getQuizAnswerCount();
            totalFocusRatio = totalFocusRatio.add(studentLecture.getFocusRatio());
            totalFocusTime += studentLecture.getFocusTime();
        }

        Integer averageQuizTotalScore = totalQuizTotalScore/studentCount;
        Integer averageQuizAnswerCount = totalQuizAnswerCount/studentCount;
        Integer averageFocusTime = totalFocusTime/studentCount;
        BigDecimal averageFocusRatio = totalFocusRatio.divide(BigDecimal.valueOf(studentCount), 2, RoundingMode.HALF_UP);

        // 몽고디비 저장 나중에 몽고디비 연결해서 해야함.
        StudentLectureStatisticDto lectureStudentStatisticDto = StudentLectureStatisticDto.of(averageQuizTotalScore, averageQuizAnswerCount, averageFocusTime, averageFocusRatio);
        studentLectureMongoRepository.save(lectureStudentStatisticDto);
        return lectureStudentStatisticDto;
    }

    private List<StudentLecture> findAllStudentInfoByLectureId(Integer lectureId) {
        return studentLectureQueryRepository.findAllStudentInfoByLectureId(lectureId);
    }
}
