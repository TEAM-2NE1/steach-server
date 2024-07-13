package com.twentyone.steachserver.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *   lectureId: 1,
 *   quizNumber : 1,
 *   question: “여행하면서 겪은 감상을 표현하는 글을 무엇이라 하는가?”,
 *   choice: [
 *     “기행문”, “소설”, “수필”, “시”
 *   ],
 *   answer: [
 *     “기행문”, “소설”
 *   ] // 중복가능
 * }
 */

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PRIVATE)
public class QuizRequestDto {
    private Integer lectureId;
    private Integer quizNumber;
    private String question;
    private List<String> choices;
    private List<String> answers;
}
