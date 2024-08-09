package com.twentyone.steachserver.domain.quiz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//나중에 redis로 변경..
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuizStatistics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer lectureId;
    private Integer studentId;
    private Integer prevRank = 0;
    private Integer prevScore = 0;
    private Integer currentRank = 0;
    private Integer currentScore = 0;

    public QuizStatistics(Integer lectureId, Integer studentId) {
        this.lectureId = lectureId;
        this.studentId = studentId;
    }

    public void update(Integer score) {
        this.prevRank = currentRank;
        this.prevScore = currentScore;
        this.currentScore += score;
    }

    public void setCurrentRank(int i) {
        this.currentRank = i;
    }
}
