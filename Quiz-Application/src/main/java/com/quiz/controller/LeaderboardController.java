package com.quiz.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.Leaderboard;
import com.quiz.entity.Quiz;
import com.quiz.entity.User;
import com.quiz.entity.dto.LeaderboardDTO;
import com.quiz.repo.LeaderboardRepository;
import com.quiz.repo.QuizRepository;
import com.quiz.repo.ResultRepository;
import com.quiz.repo.UserRepository;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    // Get leaderboard for a quiz (ordered by score DESC)
    @GetMapping("/quiz/{quizId}")
    public List<LeaderboardDTO> getLeaderboardByQuiz(@PathVariable Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return leaderboardRepository.findByQuizOrderByScoreDesc(quiz)
                .stream()
                .map(entry -> new LeaderboardDTO(
                        entry.getUser().getUsername(),
                        entry.getScore(),
                        entry.getTotalMarks()
                ))
                .toList();
    }

    // Get leaderboard for a user
    @GetMapping("/user/{userId}")
    public List<Leaderboard> getLeaderboardByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return leaderboardRepository.findByUser(user);
    }

    // Save score after quiz completion
    @PostMapping("/save")
    public Leaderboard saveScore(@RequestParam Long userId, @RequestParam Long quizId, 
                                @RequestParam int score, @RequestParam int totalMarks) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));

        Leaderboard record = leaderboardRepository.findByUserAndQuiz(user, quiz);
        if (record == null) {
            record = new Leaderboard();
            record.setUser(user);
            record.setQuiz(quiz);
        }

    record.setScore(score);
    record.setTotalMarks(totalMarks);
    return leaderboardRepository.save(record);

}
 @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        List<Object[]> leaderboardData = resultRepository.getLeaderboard();

        List<LeaderboardDTO> leaderboard = new ArrayList<>();
        for (Object[] row : leaderboardData) {
            User user = (User) row[0];
            Long totalScore = (Long) row[1]; // SUM returns Long

            leaderboard.add(new LeaderboardDTO(user.getUsername(), totalScore));
        }

        model.addAttribute("leaderboard", leaderboard);
        return "leaderboard";
    }

}
