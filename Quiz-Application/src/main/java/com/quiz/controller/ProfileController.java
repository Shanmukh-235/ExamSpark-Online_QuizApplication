package com.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.quiz.entity.Result;
import com.quiz.entity.User;
import com.quiz.repo.ResultRepository;
import com.quiz.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch results for this user
        List<Result> results = resultRepository.findByUser(user);

        // Compute stats
        int totalQuizzes = results.size();
        double avgScore = 0.0;
        int highestScore = 0;

        if (!results.isEmpty()) {
            int totalScore = 0;
            for (Result r : results) {
                totalScore += r.getScore();
                if (r.getScore() > highestScore) {
                    highestScore = r.getScore();
                }
            }
            avgScore = (double) totalScore / totalQuizzes;
        }

        // Add attributes
        model.addAttribute("user", user);
        model.addAttribute("stats", new StatsDTO(totalQuizzes, avgScore, highestScore));

        return "profile";
    }

    // Handle update profile form
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User updatedUser, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // update fields
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setCourse(updatedUser.getCourse());

        // save
        userRepository.save(user);

        // refresh session
        session.setAttribute("user", user);

        return "redirect:/profile?success";
    }

    // Inner DTO for profile stats
    public static class StatsDTO {

        private int totalQuizzes;
        private double avgScore;
        private int highestScore;

        public StatsDTO(int totalQuizzes, double avgScore, int highestScore) {
            this.totalQuizzes = totalQuizzes;
            this.avgScore = avgScore;
            this.highestScore = highestScore;
        }

        public int getTotalQuizzes() {
            return totalQuizzes;
        }

        public double getAvgScore() {
            return avgScore;
        }

        public int getHighestScore() {
            return highestScore;
        }
    }
}
