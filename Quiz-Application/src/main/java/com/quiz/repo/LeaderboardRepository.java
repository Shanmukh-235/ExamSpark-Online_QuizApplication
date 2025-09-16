package com.quiz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quiz.entity.Leaderboard;
import com.quiz.entity.Quiz;
import com.quiz.entity.User;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    List<Leaderboard> findByQuiz(Quiz quiz);
    List<Leaderboard> findByUser(User user);

    // New: Ordered leaderboard
    List<Leaderboard> findByQuizOrderByScoreDesc(Quiz quiz);
    Leaderboard findByUserAndQuiz(User user, Quiz quiz);
}

