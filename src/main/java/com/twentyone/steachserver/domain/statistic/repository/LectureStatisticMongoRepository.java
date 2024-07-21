package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.LectureStatisticsByAllStudent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureStatisticMongoRepository extends MongoRepository<LectureStatisticsByAllStudent, String> {
}
