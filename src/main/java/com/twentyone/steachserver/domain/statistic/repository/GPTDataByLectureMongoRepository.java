package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.mongo.GPTDataByLecture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GPTDataByLectureMongoRepository extends MongoRepository<GPTDataByLecture, String> {
//    List<GPTDataByLecture> findAllByStudentName(String studentName);
//    List<GPTDataByLecture> findAllByStudentNameAndLectureId(String name, Integer id);
    List<GPTDataByLecture> findAllByStudentIdAndLectureId(Integer studentId, Integer lectureId);
    List<GPTDataByLecture> findAllByStudentId(Integer studentId);
}
