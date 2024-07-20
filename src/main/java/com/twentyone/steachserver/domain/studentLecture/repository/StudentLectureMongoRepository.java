package com.twentyone.steachserver.domain.studentLecture.repository;

import com.twentyone.steachserver.domain.lecture.dto.GPTDataByLectureDto;
import com.twentyone.steachserver.domain.studentLecture.dto.StudentLectureStatisticDto;
import org.springframework.stereotype.Repository;

@Repository
public class StudentLectureMongoRepository{
    public void save(StudentLectureStatisticDto studentLectureStatisticDto) {}
    public void save(GPTDataByLectureDto gptDataByLectureDto) {}
}
