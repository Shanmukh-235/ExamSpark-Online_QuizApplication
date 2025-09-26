package com.quiz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.quiz.entity.Quiz;
import com.quiz.entity.QuizAttempt;
import com.quiz.entity.User;
import com.quiz.repo.LeaderboardRepository;
import com.quiz.repo.QuizAttemptRepository;
import com.quiz.repo.QuizRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private QuizAttemptRepository attemptRepo;

    @GetMapping("/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch quizzes matching user course OR common
        List<Quiz> quizzes = quizRepository.findByCategoryIn(List.of(user.getCourse(), "Common"));

        // Fetch leaderboard (aggregate scores)
        List<Object[]> rawLeaderboard = leaderboardRepository.findUserTotalScores();
        List<Map<String, Object>> leaderboard = new ArrayList<>();

        for (Object[] row : rawLeaderboard) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("username", row[0]);
            entry.put("totalScore", row[1]);
            leaderboard.add(entry);
        }

        // Remove quizzes already attempted
        List<QuizAttempt> attempts = attemptRepo.findByUser(user);
        Set<Long> attemptedIds = attempts.stream().map(a -> a.getQuiz().getId()).collect(Collectors.toSet());

        quizzes.removeIf(quiz -> attemptedIds.contains(quiz.getId()));

        model.addAttribute("user", user);
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("leaderboard", leaderboard);

        return "user-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj == null || !"ADMIN".equalsIgnoreCase(((User) userObj).getRole())) {
            return "redirect:/login";
        }
        return "admin-dashboard";
    }

    @GetMapping("/user/performances")
    public String myPerformances(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch all attempts for this user
        List<QuizAttempt> attempts = attemptRepo.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("attempts", attempts);

        return "user-performances"; // this will be a new Thymeleaf template
    }

    // @GetMapping("/logout")
    // public String logout(HttpSession session) {
    //     session.invalidate();  // clear user session
    //     return "redirect:/";   // redirect to index 
    // }
}
