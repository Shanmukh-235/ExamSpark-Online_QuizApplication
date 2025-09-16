package com.quiz.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.Question;
import com.quiz.entity.Quiz;
import com.quiz.entity.dto.QuestionDTO;
import com.quiz.repo.QuestionRepository;
import com.quiz.repo.QuizRepository;

@RestController
@RequestMapping("/api/admin/questions")
public class AdminQuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    // @GetMapping
    // public List<Question> getAllQuestions() {
    //     return questionRepository.findAll();
    // }

    @GetMapping
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(q -> new QuestionDTO(
                        q.getId(),
                        q.getQuestionText(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD(),
                        q.getCorrectAnswer(),
                        q.getQuiz() != null ? q.getQuiz().getId() : null,
                        q.getQuiz() != null ? q.getQuiz().getTitle() : "Unknown"
                ))
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody Question question) {
        if (question.getQuiz() == null || question.getQuiz().getId() == null) {
            return ResponseEntity.badRequest().body("Quiz ID is required");
        }

        Quiz quiz = quizRepository.findById(question.getQuiz().getId())
                .orElse(null);

        if (quiz == null) {
            return ResponseEntity.badRequest().body("Quiz not found with id " + question.getQuiz().getId());
        }

        question.setQuiz(quiz);
        Question saved = questionRepository.save(question);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion) {
        Optional<Question> opt = questionRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Question question = opt.get();
        if (updatedQuestion.getQuestionText() != null) question.setQuestionText(updatedQuestion.getQuestionText());
        if (updatedQuestion.getOptionA() != null) question.setOptionA(updatedQuestion.getOptionA());
        if (updatedQuestion.getOptionB() != null) question.setOptionB(updatedQuestion.getOptionB());
        if (updatedQuestion.getOptionC() != null) question.setOptionC(updatedQuestion.getOptionC());
        if (updatedQuestion.getOptionD() != null) question.setOptionD(updatedQuestion.getOptionD());
        if (updatedQuestion.getCorrectAnswer() != null) question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());

        if (updatedQuestion.getQuiz() != null && updatedQuestion.getQuiz().getId() != null) {
            Optional<Quiz> quizOpt = quizRepository.findById(updatedQuestion.getQuiz().getId());
            if (quizOpt.isEmpty()) return ResponseEntity.badRequest().body("Quiz not found");
            question.setQuiz(quizOpt.get());
        }

        return ResponseEntity.ok(questionRepository.save(question));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        if (!questionRepository.existsById(id)) return ResponseEntity.notFound().build();
        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    
}
