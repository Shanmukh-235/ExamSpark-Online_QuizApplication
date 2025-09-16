package com.quiz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quiz.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Find quizzes by category
    List<Quiz> findByCategoryIn(List<String> categories);

    // Find only active quizzes (for users)
    List<Quiz> findByActiveTrue();

    // Find quizzes by title (case-insensitive)
    List<Quiz> findByTitleContainingIgnoreCase(String title);


    // Fetch quizzes NOT attempted by a given user
    @Query("SELECT q FROM Quiz q WHERE q.id NOT IN (SELECT l.quiz.id FROM Leaderboard l WHERE l.user.id = :userId)")
    List<Quiz> findAvailableQuizzesForUser(@Param("userId") Long userId);
}



