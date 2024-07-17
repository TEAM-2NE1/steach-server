package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.lecture.model.Lecture;
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

    public Optional<Lecture> findLectureById(Integer id) {
        return lectureRepository.findById(id);
    }

    @Override
    public List<Lecture> upcomingLecture(int toMinute, int fromMinute) {
        LocalDateTime toTime = LocalDateTime.now().plusMinutes(toMinute);
        LocalDateTime fromTime = LocalDateTime.now().plusMinutes(toMinute);

        return lectureRepository.findByLectureStartTimeBetween(fromTime, toTime);
    }
}
