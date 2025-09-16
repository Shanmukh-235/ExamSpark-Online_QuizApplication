package com.quiz.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.quiz.entity.Quiz;
import com.quiz.entity.QuizAttempt;
import com.quiz.entity.User;
import com.quiz.repo.QuizAttemptRepository;
import com.quiz.repo.QuizRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private QuizRepository quizRepository;

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

    // Remove quizzes already attempted
    List<QuizAttempt> attempts = attemptRepo.findByUser(user);
    Set<Long> attemptedIds = attempts.stream()
            .map(a -> a.getQuiz().getId())
            .collect(Collectors.toSet());

    quizzes.removeIf(quiz -> attemptedIds.contains(quiz.getId()));

    model.addAttribute("user", user);
    model.addAttribute("quizzes", quizzes);

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
}
