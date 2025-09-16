package com.quiz.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quiz.entity.Quiz;
import com.quiz.repo.QuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public List<Quiz> getAvailableQuizzes(Long userId) {
        return quizRepository.findAvailableQuizzesForUser(userId);
    }
}
