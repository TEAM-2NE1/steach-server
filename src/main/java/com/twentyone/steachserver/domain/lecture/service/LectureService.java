package com.twentyone.steachserver.domain.lecture.service;



import com.twentyone.steachserver.domain.lecture.model.Lecture;

import java.util.Optional;

public interface LectureService {
    public Optional<Lecture> findLectureById(Integer id);
}
