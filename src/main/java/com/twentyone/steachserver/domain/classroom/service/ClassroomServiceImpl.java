package com.twentyone.steachserver.domain.classroom.service;

import com.twentyone.steachserver.domain.classroom.dto.FinalClassroomRequestDto;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRooms;
import com.twentyone.steachserver.domain.classroom.repository.ClassroomRepository;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureQueryRepository;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import com.twentyone.steachserver.domain.member.model.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private ClassroomRepository classroomRepository;
    private LectureQueryRepository lectureQueryRepository;

    private LectureService lectureService;
    private StudentLectureService studentLectureService;

    @Override
    @Transactional
    public UpComingClassRooms upcomingClassroom() {
        // 분 단위로 값 받아서 해주자. 남은 시간이 90분에서 ~ 30분 사이꺼 가져오기

        // jpa 에서 가져오면 될듯
        List<Lecture> lectures = lectureService.upcomingLecture(30,90);

        UpComingClassRooms classrooms = UpComingClassRooms.createEmptyUpComingClassRooms();

        // 추후 배치처리해주면 기가 맥힐듯
        for (Lecture lecture : lectures) {
            Classroom classroom = Classroom.createClassroom(lecture);
            classroomRepository.save(classroom);
            classrooms.addClassroom(classroom);
        }

        return classrooms;
    }

    @Override
    public Optional<Classroom> getClassroomByLectureAndStudent(Integer lectureId, Integer studentId) {
        return lectureQueryRepository.findClassroomByLectureAndStudent(lectureId, studentId);
    }


}
