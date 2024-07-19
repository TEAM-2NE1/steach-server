package com.twentyone.steachserver.domain.studentCurriculum.service;

import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.repository.StudentCurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentCurriculumServiceImpl implements StudentCurriculumService{
    private final StudentCurriculumRepository studentCurriculumRepository;

    @Override
    public Optional<StudentCurriculum> findById(Integer id) {

        return studentCurriculumRepository.findById(id);
    }
}
