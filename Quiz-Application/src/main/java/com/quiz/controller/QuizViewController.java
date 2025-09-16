package com.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.quiz.repo.QuizRepository;

@Controller
public class QuizViewController {

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/quizzes")
    public String showQuizzes(Model model) {
        model.addAttribute("quizzes", quizRepository.findAll());
        return "user-dashboard"; // src/main/resources/templates/quizList.html
    }
}

