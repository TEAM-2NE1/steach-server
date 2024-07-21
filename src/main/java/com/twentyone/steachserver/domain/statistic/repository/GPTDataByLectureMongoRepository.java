package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.mongo.GPTDataByLecture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GPTDataByLectureMongoRepository extends MongoRepository<GPTDataByLecture, String> {
    Optional<GPTDataByLecture> findByLectureIdAndStudentId(Integer lectureId, Integer studentId);
}
