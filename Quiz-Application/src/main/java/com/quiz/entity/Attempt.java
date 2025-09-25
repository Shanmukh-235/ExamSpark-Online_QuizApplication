package com.quiz.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;   
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  
@Entity
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
    @ManyToOne
    private User user;

    @ManyToOne
    private Quiz quiz;

    private int score;
    private int totalMarks;

    private LocalDateTime attemptedAt;
}
