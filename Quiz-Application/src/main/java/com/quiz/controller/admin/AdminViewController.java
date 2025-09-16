package com.quiz.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @GetMapping
    public String dashboard() {
        return "admin-dashboard";
    }
}

// admin-users.html → Show list of users + delete/edit buttons.
// admin-quizzes.html → Show quizzes + add/edit/delete.
// admin-questions.html → Manage questions for quizzes.
// admin-leaderboard.html → View leaderboard per quiz.