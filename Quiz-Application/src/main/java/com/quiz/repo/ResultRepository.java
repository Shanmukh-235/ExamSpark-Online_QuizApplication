package com.quiz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quiz.entity.Result;
import com.quiz.entity.User;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    // Get results for a specific user
    List<Result> findByUserId(Long userId);

    // Get results for a quiz, ordered by score descending (leaderboard)
    List<Result> findByQuizIdOrderByScoreDesc(Long quizId);

    List<Result> findByUser(User user);
}
