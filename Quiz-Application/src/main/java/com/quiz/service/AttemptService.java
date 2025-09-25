package com.quiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.entity.Attempt;
import com.quiz.entity.User;
import com.quiz.repo.AttemptRepository;

@Service
public class AttemptService {
    @Autowired
    private AttemptRepository attemptRepository;

    public List<Attempt> getUserAttempts(User user) {
        return attemptRepository.findByUser(user);
    }
}
