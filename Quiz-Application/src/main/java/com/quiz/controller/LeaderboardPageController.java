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
        // fetch all records sorted by score
        var topScores = leaderboardRepository.findAll()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .limit(10)
                .toList();

        model.addAttribute("topScores", topScores);
        return "leaderboard"; // Thymeleaf template
    }
}
