// package com.quiz.controller.admin;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.quiz.entity.Quiz;
// import com.quiz.repo.QuizRepository;

// @RestController
// @RequestMapping("/api/admin/quizzes")
// public class AdminQuizController {

//     @Autowired
//     private QuizRepository quizRepository;

//     // Get all quizzes
//     @GetMapping
//     public List<Quiz> getAllQuizzes() {
//         return quizRepository.findAll();
//     }

//     // Create quiz
//     @PostMapping
//     public Quiz createQuiz(@RequestBody Quiz quiz) {
//         return quizRepository.save(quiz);
//     }

//     // Update quiz
//     @PutMapping("/{id}")
//     public ResponseEntity<?> editQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
//         Optional<Quiz> optionalQuiz = quizRepository.findById(id);
//         if (optionalQuiz.isPresent()) {
//             Quiz quiz = optionalQuiz.get();
//             if (updatedQuiz.getTitle() != null) quiz.setTitle(updatedQuiz.getTitle());
//             if (updatedQuiz.getCategory() != null) quiz.setCategory(updatedQuiz.getCategory());
//             if (updatedQuiz.getDescription() != null) quiz.setDescription(updatedQuiz.getDescription());
//             quiz.setActive(updatedQuiz.isActive());
//             Quiz savedQuiz = quizRepository.save(quiz);
//             return ResponseEntity.ok(savedQuiz);
//         } else {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
//         }
//     }

//     // Delete quiz
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
//         if (quizRepository.existsById(id)) {
//             quizRepository.deleteById(id);
//             return ResponseEntity.ok("Quiz deleted");
//         }
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
//     }
// }

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

import com.quiz.entity.Quiz;
import com.quiz.repo.QuizRepository;

@RestController
@RequestMapping("/api/admin/quizzes")
public class AdminQuizController {

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizRepository.save(quiz));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
        Optional<Quiz> opt = quizRepository.findById(id);
        if (opt.isEmpty())
            return ResponseEntity.notFound().build();

        Quiz quiz = opt.get();
        if (updatedQuiz.getTitle() != null)
            quiz.setTitle(updatedQuiz.getTitle());
        if (updatedQuiz.getCategory() != null)
            quiz.setCategory(updatedQuiz.getCategory());
        quiz.setActive(updatedQuiz.isActive());

        return ResponseEntity.ok(quizRepository.save(quiz));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        if (!quizRepository.existsById(id))
            return ResponseEntity.notFound().build();
        quizRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
