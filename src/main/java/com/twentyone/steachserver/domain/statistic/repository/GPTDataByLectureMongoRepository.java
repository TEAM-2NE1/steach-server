package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.GPTDataByLecture;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GPTDataByLectureMongoRepository extends MongoRepository<GPTDataByLecture, String> {
}
