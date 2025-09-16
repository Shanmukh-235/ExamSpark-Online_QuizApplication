package com.quiz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quiz.entity.Leaderboard;
import com.quiz.entity.Question;
import com.quiz.entity.Quiz;
import com.quiz.entity.QuizAttempt;
import com.quiz.entity.User;
import com.quiz.repo.LeaderboardRepository;
import com.quiz.repo.QuestionRepository;
import com.quiz.repo.QuizAttemptRepository;
import com.quiz.repo.QuizRepository;
import com.quiz.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private final QuizRepository quizRepo;
    @Autowired
    private final LeaderboardRepository leaderboardRepo;
    @Autowired
    private final QuestionRepository questionRepo;
    @Autowired
    private final QuizAttemptRepository attemptRepo;
    @Autowired
    @SuppressWarnings("unused")
    private final UserRepository userRepo;

    public QuizController(QuizRepository quizRepo, QuestionRepository questionRepo, QuizAttemptRepository attemptRepo,
            UserRepository userRepo, LeaderboardRepository leaderboardRepo) {
        this.quizRepo = quizRepo;
        this.leaderboardRepo = leaderboardRepo;
        this.questionRepo = questionRepo;
        this.attemptRepo = attemptRepo;
        this.userRepo = userRepo;
    }

    // Redirect /quiz/{id} → /quiz/{id}/start
    @GetMapping("/{id}")
    public String redirectToStart(@PathVariable Long id) {
        return "redirect:/quiz/" + id + "/start";
    }

    // Show quiz with all questions
    @GetMapping("/{id}/start")
    public String startQuiz(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepo.findById(id).orElseThrow();
        List<Question> questions = questionRepo.findByQuiz(quiz);
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        return "take-quiz"; // Thymeleaf page
    }

    // Handle submission
    @PostMapping("/{id}/submit")
    public String submitQuiz(@PathVariable Long id,
            @RequestParam Map<String, String> responses,
            HttpSession session,
            Model model) {

        Quiz quiz = quizRepo.findById(id).orElseThrow();
        List<Question> questions = questionRepo.findByQuiz(quiz);

        int score = 0;
        for (Question q : questions) {
            String answer = responses.get("q" + q.getId());
            if (answer != null && answer.equalsIgnoreCase(q.getCorrectAnswer())) {
                score++;
            }
        }

        // Get logged-in user from session
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // user not logged in
        }

        // Save attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setUser(user);
        attempt.setScore(score);
        attempt.setTotalMarks(questions.size());
        attemptRepo.save(attempt);

        // Save leaderboard
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setQuiz(quiz);
        leaderboard.setUser(user);
        leaderboard.setScore(score);
        leaderboard.setTotalMarks(questions.size());
        leaderboardRepo.save(leaderboard);

        // Pass data to success page
        model.addAttribute("quiz", quiz);
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());

        return "quiz-success";
    }


}
