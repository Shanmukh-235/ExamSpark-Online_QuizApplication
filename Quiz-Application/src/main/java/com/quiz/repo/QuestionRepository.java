package com.quiz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quiz.entity.Question;
import com.quiz.entity.Quiz;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Get all questions by quiz
    List<Question> findByQuiz_Id(Long quizId);
    List<Question> findByQuizId(Long quizId);
    List<Question> findByQuiz(Quiz quiz);
}
