package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.mongo.GPTDataByLecture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GPTDataByLectureMongoRepository extends MongoRepository<GPTDataByLecture, String> {
    List<GPTDataByLecture> findAllByStudentIdAndLectureId(Integer studentId, Integer lectureId);
    List<GPTDataByLecture> findAllByStudentId(Integer studentId);
}
