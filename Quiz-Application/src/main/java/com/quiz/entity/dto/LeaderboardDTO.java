package com.quiz.entity.dto;

public class LeaderboardDTO {

    private String username;
    private int score;
    private int totalMarks;

    public LeaderboardDTO(String username, int score, int totalMarks) {
        this.username = username;
        this.score = score;
        this.totalMarks = totalMarks;
    }

    public LeaderboardDTO(String username2, Long totalScore) {
        //TODO Auto-generated constructor stub
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
}
