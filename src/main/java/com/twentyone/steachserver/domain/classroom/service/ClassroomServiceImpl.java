package com.twentyone.steachserver.domain.classroom.service;

import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRoom;
import com.twentyone.steachserver.domain.classroom.dto.UpComingClassRooms;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.repository.ClassroomQueryRepository;
import com.twentyone.steachserver.domain.classroom.repository.ClassroomRepository;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final ClassroomQueryRepository classroomQueryRepository;

    private final LectureService lectureService;
    private final LectureRepository lectureRepository;


    @Override
    @Transactional
    public UpComingClassRooms upcomingClassroom() {
        // 분 단위로 값 받아서 해주자. 남은 시간이 90분에서 ~ 30분 사이꺼 가져오기
        List<Lecture> lectures = lectureService.upcomingLecture(30,90);

        UpComingClassRooms classrooms = UpComingClassRooms.createEmptyUpComingClassRooms();

        for (Lecture lecture : lectures) {
            Classroom classroom = classroomRepository.findByLectureId(lecture.getId())
                            .orElseGet(() -> Classroom.createClassroom(lecture));
            classrooms.addClassroom(UpComingClassRoom.of(classroom.getSessionId()));
        }

        return classrooms;
    }

    @Transactional
    @Scheduled(fixedDelay = 3600000)
    public void createUpcomingClassroom() {
        List<Lecture> lectures = lectureService.upcomingLecture(40,100);

        // 추후 배치처리해주면 기가 맥힐듯
        for (Lecture lecture : lectures) {
            Classroom classroom = Classroom.createClassroom(lecture);
            classroomRepository.save(classroom);
        }
    }

    public Optional<Classroom> getClassroomBySessionIdAndStudent(Integer studentId, String sessionId) {
        return classroomQueryRepository.findClassroomBySessionIdAndStudent(studentId, sessionId);
    }
}
