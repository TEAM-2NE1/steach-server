package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.lecture.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    public Optional<Lecture> findLectureById(Integer id) {
        return lectureRepository.findById(id);
    }
}
