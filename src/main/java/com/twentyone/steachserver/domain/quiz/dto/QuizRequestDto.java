package com.twentyone.steachserver.domain.quiz.dto;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizRequestDto {
    private Integer quizNumber;
    private String question;
    private List<String> choices;
    @Min(1)
    private Integer answers;

    public Integer getQuizNumber() {
        return quizNumber;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public Integer getAnswers() {
        return answers - 1; //서버용
    }
}
