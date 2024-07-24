package com.twentyone.steachserver.domain.statistic.repository;

import com.twentyone.steachserver.domain.statistic.model.mongo.LectureStatisticsByAllStudent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureStatisticMongoRepository extends MongoRepository<LectureStatisticsByAllStudent, String> {
    // Todo: 하나만 반환되야할거 같아서 생성할때 하나만 될 수 있도록 해야할거 같음. 맨 마지막데이터가 유지되도록?? 아니면 처음??
    // 리스트 받아와서 있는거 다 지우고 하나만 해주는 방식 선택 오류 터지면 곤란
    List<LectureStatisticsByAllStudent> findAllByLectureId(Integer lectureId);
    Optional<LectureStatisticsByAllStudent> findByLectureId(Integer lectureId);

}
