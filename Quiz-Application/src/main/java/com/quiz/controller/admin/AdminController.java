package com.quiz.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.Question;
import com.quiz.repo.QuestionRepository;


@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private QuestionRepository questionRepository;
    // get all questions for a quiz
    @GetMapping("/questions/{quizId}")
    public List<Question> getQuestionsByQuiz(@PathVariable Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    // OR get all questions without filtering
    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}
