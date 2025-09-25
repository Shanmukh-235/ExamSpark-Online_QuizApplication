package com.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.quiz.repo.LeaderboardRepository;

@Controller
public class LeaderboardPageController {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @GetMapping("/leaderboard")
    public String leaderboardPage(Model model) {
        // Fetch top 10 by score directly from repository
        var topScores = leaderboardRepository.findTop10ByOrderByScoreDesc();
        model.addAttribute("topScores", topScores);
        return "leaderboard"; // Thymeleaf template
    }
}
