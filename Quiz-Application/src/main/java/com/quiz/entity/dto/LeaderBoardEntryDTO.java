package com.quiz.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderBoardEntryDTO {
    private String username;
    private int score;
    private int totalMarks;
}
