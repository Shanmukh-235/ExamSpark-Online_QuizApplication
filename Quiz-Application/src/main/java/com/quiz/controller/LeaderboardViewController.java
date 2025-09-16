package com.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.quiz.entity.Leaderboard;
import com.quiz.entity.Quiz;
import com.quiz.repo.LeaderboardRepository;
import com.quiz.repo.QuizRepository;

@Controller
public class LeaderboardViewController {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private QuizRepository quizRepository;

    // Show leaderboard for a quiz
    @GetMapping("/quiz/{quizId}/leaderboard")
    public String showLeaderboard(@PathVariable Long quizId, Model model) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Leaderboard> leaderboard = leaderboardRepository.findByQuizOrderByScoreDesc(quiz);
        model.addAttribute("quiz", quiz);
        model.addAttribute("leaderboard", leaderboard != null ? leaderboard : List.of());
        return "leaderboard";
    }
}
