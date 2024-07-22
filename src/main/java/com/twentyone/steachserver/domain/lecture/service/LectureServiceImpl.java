package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.lecture.dto.*;
import com.twentyone.steachserver.domain.lecture.dto.update.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureQueryRepository;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;

import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureQueryRepository lectureQueryRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;

    @Override
    public List<Lecture> upcomingLecture(int toMinute, int fromMinute) {
        LocalDateTime toTime = LocalDateTime.now().plusMinutes(toMinute);
        LocalDateTime fromTime = LocalDateTime.now().plusMinutes(toMinute);

        return lectureRepository.findByLectureStartDateBetween(fromTime, toTime);
    }


    @Override
    public LectureBeforeStartingResponseDto getLectureInformation(Integer lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));

        LectureBeforeStartingResponseDto lectureBeforeStartingResponse = lectureQueryRepository.getLectureBeforeStartingResponse(lectureId);

        if(lecture.getRealEndTime() == null){
            return lectureBeforeStartingResponse;
        }

        List<StudentInfoByLectureDto> studentInfoByLecture = studentLectureQueryRepository.getStudentInfoByLecture(lectureId);

        CompletedLecturesResponseDto completedLecturesResponseDto = CompletedLecturesResponseDto.of(lectureBeforeStartingResponse, studentInfoByLecture, lecture);
        completedLecturesResponseDto.completeLecture();

        return completedLecturesResponseDto;
    }

    @Override
    public Optional<LectureBeforeStartingResponseDto> updateLectureInformation(Integer lectureId, UpdateLectureRequestDto lectureRequestDto) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));

        lecture.update(lectureRequestDto);
        return Optional.ofNullable(lectureQueryRepository.getLectureBeforeStartingResponse(lectureId));
    }

    @Override
    public Lecture updateRealEndTime(Integer lectureId) {
        return lectureRepository.findById(lectureId)
                .map(lecture -> { lecture.updateRealEndTimeWithNow(); return lecture; })
                .orElseThrow(() -> new IllegalArgumentException("lecture not found"));
    }

    @Override
    public FinalLectureInfoByTeacherDto getFinalLectureInformation(Integer lectureId){
        return lectureQueryRepository.getFinalLectureInfoByTeacher(lectureId);
    }
}

