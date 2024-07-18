package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureQueryRepository;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
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

    public Optional<Lecture> findLectureById(Integer id) {
        return lectureRepository.findById(id);
    }

    @Override
    public List<Lecture> upcomingLecture(int toMinute, int fromMinute) {
        LocalDateTime toTime = LocalDateTime.now().plusMinutes(toMinute);
        LocalDateTime fromTime = LocalDateTime.now().plusMinutes(toMinute);

        return lectureRepository.findByLectureStartTimeBetween(fromTime, toTime);
    }

    // Optional 로 줘야하나?
    @Override
    public LectureBeforeStartingResponseDto getLectureInformation(Integer lectureId) {
        return lectureQueryRepository.findLectureDetailsByLectureId(lectureId);
    }

    @Override
    public LectureBeforeStartingResponseDto updateLectureInformation(Integer lectureId, UpdateLectureRequestDto lectureRequestDto) {
        Optional<Lecture> lectureById = findLectureById(lectureId);
        if (lectureById.isPresent()) {
            Lecture lecture = lectureById.get();
            lecture.update(lectureRequestDto);
            return lectureQueryRepository.findLectureDetailsByLectureId(lectureId);
        }
        return null;
    }

    @Override
    public FinalLectureInfoByTeacherDto getFinalLectureInformation(Integer lectureId) {
        return null;
    }

    @Override
    public void updateRealEndTime(Integer lectureId) {
        Optional<Lecture> lectureById = findLectureById(lectureId);
        if (lectureById.isPresent()) {
            Lecture lecture = lectureById.get();
            lecture.updateRealEndTimeWithNow();
        }
    }
}


