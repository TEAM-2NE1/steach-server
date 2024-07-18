package com.twentyone.steachserver.config;

import com.twentyone.steachserver.domain.lecture.repository.LectureQueryRepository;
import com.twentyone.steachserver.domain.lectureStudent.repository.LectureStudentQueryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConstructConfiguration {

    private final EntityManager em;

    @Bean
    public LectureQueryRepository lectureQueryRepository() {
        return new LectureQueryRepository(em);
    }

    @Bean
    public LectureStudentQueryRepository lectureStudentQueryRepository() {
        return new LectureStudentQueryRepository(em);
    }

}
