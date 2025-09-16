package com.quiz.controller;

import com.quiz.entity.Quiz;
import com.quiz.entity.Result;
import com.quiz.entity.User;
import com.quiz.repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository; // add this

    @Autowired
    private QuizRepository quizRepository; // add this

    // Get results by user
    @GetMapping("/user/{userId}")
    public List<Result> getResultsByUser(@PathVariable Long userId) {
        return resultRepository.findByUserId(userId);
    }

    // Leaderboard for a quiz
    @GetMapping("/quiz/{quizId}/leaderboard")
    public List<Result> getLeaderboard(@PathVariable Long quizId) {
        return resultRepository.findByQuizIdOrderByScoreDesc(quizId);
    }

    // Submit a result
    @PostMapping
    public ResponseEntity<?> submitResult(@RequestBody Map<String, Object> payload) {
        Long quizId = Long.valueOf(payload.get("quizId").toString());
        Integer score = Integer.valueOf(payload.get("score").toString());
        Long userId = Long.valueOf(payload.get("userId").toString()); // frontend sends logged-in user id
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Access denied. Admins only.");
        }
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        Result result = new Result();
        result.setScore(score);
        result.setUser(user);
        result.setQuiz(quiz);
        result.setSubmittedAt(LocalDateTime.now());
        return ResponseEntity.ok(resultRepository.save(result));
    }
}
