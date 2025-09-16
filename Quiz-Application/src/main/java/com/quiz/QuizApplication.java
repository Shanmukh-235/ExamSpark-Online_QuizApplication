package com.quiz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.quiz.entity.User;
import com.quiz.repo.UserRepository;

@SpringBootApplication
public class QuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);

        System.out.println("Applicaion works successfully!");
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository) {
        return (args) -> {
            if (!userRepository.existsByUsername("admin") && !userRepository.existsByEmail("admin@ex.com")) {
                User admin = new User(null, "admin", "admin@ex.com", "admin123", "ADMIN", null,0, 0);
                userRepository.save(admin);
                System.out.println("Working successfully!");
            // } else {
            //     System.out.println("Admin user already exists.");
            }
        };
    }
}
