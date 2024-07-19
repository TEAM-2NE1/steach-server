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
public record QuizRequestDto(Integer quizNumber, String question, List<String> choices, List<String> answers) {
    public static QuizRequestDto createQuizRequestDto(Integer quizNumber, String question, List<String> choices, List<String> answers) {
        return new QuizRequestDto(quizNumber, question, choices, answers);
    }
}
