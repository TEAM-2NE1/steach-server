package com.twentyone.steachserver.domain.lecture.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyLectureHistoryResponse {
    private List<LectureHistoryResponse> history;
}
