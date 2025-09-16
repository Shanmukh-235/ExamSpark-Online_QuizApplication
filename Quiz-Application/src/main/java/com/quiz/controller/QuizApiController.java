package com.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.Quiz;
import com.quiz.repo.QuizRepository;

@RestController
@RequestMapping("/api/quizzes")
public class QuizApiController {

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
}
