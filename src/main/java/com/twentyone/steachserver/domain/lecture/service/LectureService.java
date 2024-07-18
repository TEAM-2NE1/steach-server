package com.twentyone.steachserver.domain.lecture.service;



import com.twentyone.steachserver.domain.lecture.dto.FinalLectureInfoByTeacherDto;
import com.twentyone.steachserver.domain.lecture.dto.LectureBeforeStartingResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.UpdateLectureRequestDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureService {
    public Optional<Lecture> findLectureById(Integer id);

    List<Lecture> upcomingLecture(int toMinute, int fromMinute);

    LectureBeforeStartingResponseDto getLectureInformation(Integer lectureId);

    LectureBeforeStartingResponseDto updateLectureInformation(Integer lectureId, UpdateLectureRequestDto lectureRequestDto);

    FinalLectureInfoByTeacherDto getFinalLectureInformation(Integer lectureId);

    void updateRealEndTime(Integer lectureId);
}
