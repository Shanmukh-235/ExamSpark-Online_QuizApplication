package com.quiz.entity.dto;

public class PerformanceDTO {
    private String quizName;
    private int score;
    private int totalMarks;

    public PerformanceDTO(String quizName, int score, int totalMarks) {
        this.quizName = quizName;
        this.score = score;
        this.totalMarks = totalMarks;
    }

    public String getQuizName() {
        return quizName;
    }

    public int getScore() {
        return score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }
}
