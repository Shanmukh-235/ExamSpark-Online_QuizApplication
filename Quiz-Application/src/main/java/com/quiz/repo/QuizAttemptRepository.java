package com.quiz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quiz.entity.Quiz;
import com.quiz.entity.QuizAttempt;
import com.quiz.entity.User;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUser(User user);
    List<QuizAttempt> findByQuiz(Quiz quiz);
    List<QuizAttempt> findByQuizIdOrderByScoreDesc(Long quizId);
}


