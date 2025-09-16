package com.quiz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quiz.entity.Quiz;
import com.quiz.entity.Result;
import com.quiz.entity.User;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    // Get results for a specific user
    List<Result> findByUserId(Long userId);

    // Get results for a quiz, ordered by score descending (leaderboard)
    List<Result> findByQuizIdOrderByScoreDesc(Long quizId);

    List<Result> findByUser(User user);

    boolean existsByUserAndQuiz(User user, Quiz quiz);

    @Query("SELECT r.user, SUM(r.score) as totalScore " + "FROM Result r GROUP BY r.user ORDER BY totalScore DESC")
    List<Object[]> getLeaderboard();
}
