// package com.quiz.seeder;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// import com.quiz.entity.User;
// import com.quiz.repo.UserRepository;

// import jakarta.annotation.PostConstruct;

// @Component
// public class AdminSeeder {

//     @Autowired
//     private UserRepository userRepository;

//     @PostConstruct
//     public void init() {
//         if (!userRepository.existsByUsername("admin")) {
//             User admin = new User();
//             admin.setUsername("admin");
//             admin.setEmail("admin@ex.com");
//             admin.setPassword("admin123"); // hash if needed
//             admin.setRole("ADMIN");
//             userRepository.save(admin);
//             System.out.println("Default admin user created!");
//         }
//     }
// }

