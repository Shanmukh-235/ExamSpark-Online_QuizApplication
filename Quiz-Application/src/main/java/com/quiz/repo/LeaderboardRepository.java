package com.quiz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quiz.entity.Leaderboard;
import com.quiz.entity.Quiz;
import com.quiz.entity.User;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    List<Leaderboard> findByQuiz(Quiz quiz);
    List<Leaderboard> findByUser(User user);
    List<Leaderboard> findTop10ByOrderByScoreDesc();
    // New: Ordered leaderboard
    List<Leaderboard> findByQuizOrderByScoreDesc(Quiz quiz);
    Leaderboard findByUserAndQuiz(User user, Quiz quiz);
    @Query("SELECT l.user.username AS username, SUM(l.score) AS totalScore " +
           "FROM Leaderboard l " +
           "GROUP BY l.user.username " +
           "ORDER BY totalScore DESC")
    List<Object[]> findUserTotalScores();
}

